package com.github.DarkVanityOfLight.ChattPlugin.chats

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PlayerChat : Chat{
    override var format : String
    private var radius : Int = 0
    var ignoreWorld = true

    constructor(name : String, list_style : String, ignoreWorld : Boolean, format : String, muteable : Boolean, radius : Int){
        this.format = format
        this.ignoreWorld = ignoreWorld
        this.radius = radius
    }
    constructor(name: String, list_style: String, format: String, muteable: Boolean, radius: Int){
        this.radius = radius
        this.format = format
    }

    @Deprecated("Edit the message in the event don't send a new one")
    override fun sendMessage(message : String, sender : Player, channelName: String?){
        val players : List<Player> = getPlayersInRange(sender)
        val assembledMessage : String = assembleMessage(message, sender)

        if (ignoreWorld) {
            Bukkit.getServer().broadcastMessage(assembledMessage)
        } else {
            for (player in players){
                player.sendMessage(assembledMessage)
            }
        }
    }

    fun getPlayersInRange(sender: Player) : List<Player>{
        val players = mutableListOf<Player>()

        if (this.ignoreWorld){
            return Bukkit.getOnlinePlayers().toList()
        }

        for (player in Bukkit.getOnlinePlayers()){
            if (player.location.distance(sender.location) <= this.radius){
                players.add(player)
            }
        }

        return players
    }
}