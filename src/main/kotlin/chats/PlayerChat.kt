package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PlayerChat : Chat{
    override var format : String
    private var radius : Int = 0
    var ignoreWorld = true
    override lateinit var main : Main

    constructor(name : String, list_style : String, ignoreWorld : Boolean, format : String, muteable : Boolean, radius : Int, main: Main){
        this.format = format
        this.ignoreWorld = ignoreWorld
        this.radius = radius
        this.main = main
    }
    constructor(name: String, list_style: String, format: String, muteable: Boolean, radius: Int, main: Main){
        this.radius = radius
        this.format = format
        this.main = main
    }

    override fun sendMessage(message : String, sender : Player, channelName: String?){
        val players : List<Player> = getPlayersInRange(sender)
        val assembledMessage : String = assembleMessage(message, sender)

        if (!ignoreWorld){
            for (player in players){
                player.sendMessage(assembledMessage)
            }
        } else{
            Bukkit.getOnlinePlayers().forEach { it -> it.sendMessage(message) }
        }

    }

    private fun getPlayersInRange(sender: Player) : List<Player>{
        val players = mutableListOf<Player>()

        if (this.ignoreWorld){
            return Bukkit.getOnlinePlayers().toList()
        }

        for (player in Bukkit.getOnlinePlayers()){
            if (player.location.distance(sender.location) <= this.radius){
                players.add(player)
            }
        }

        players.add(sender)
        return players
    }
}