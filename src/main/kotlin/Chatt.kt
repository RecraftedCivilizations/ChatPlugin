package com.github.DarkVanityOfLight.ChattPlugin

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class Chatt {
    private var format: String
    private var radius : Int = 0
    private var ignoreWorld = true

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
        val form = format
        form.replace("%player_name%", player.name)
        form.replace("%message%", message)
        return form
    }

    private fun getPlayersInRange(sender: Player) : List<Player>{
        val players = mutableListOf<Player>()

        for (player in Bukkit.getOnlinePlayers()){
            if (player.location.distance(sender.location) <= this.radius){
                players.add(player)
            }
        }

        return players
    }
}