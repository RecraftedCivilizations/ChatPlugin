package com.github.DarkVanityOfLight.ChattPlugin

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.properties.Delegates

class Chatt {
    lateinit var format: String
    var radius : Int = 0
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

    fun sendMessage(message : String, sender : Player){
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

    private fun assembleMessage(message: String, player : Player) : String{
        var form = format
        form.replace("%player_name%", player.name)
        form.replace("%message%", message)
        return form
    }

    private fun getPlayersInRange(sender: Player) : List<Player>{
        var players = mutableListOf<Player>()

        for (player in Bukkit.getOnlinePlayers()){
            if (player.location.distance(sender.location) <= this.radius){
                players.add(player)
            }
        }

        return players
    }
}