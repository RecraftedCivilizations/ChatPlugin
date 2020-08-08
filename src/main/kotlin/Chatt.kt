package com.github.DarkVanityOfLight.ChattPlugin

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import kotlin.properties.Delegates

class Chatt {
    lateinit var format: String
    var ignoreWorld = true

    constructor(name : String, list_style : String, ignoreWorld : Boolean, format : String, muteable : Boolean){
        this.format = format
        this.ignoreWorld = ignoreWorld
    }
    constructor(name: String, list_style: String, format: String, muteable: Boolean){}

    fun sendMessage(message : String, sender : Player){
        val players : List<Player> = getPlayersInRange(sender)
        val assmbledMessage : String = assembleMessage(message, sender)

        if (ignoreWorld) {
            Bukkit.getServer().broadcastMessage(assmbledMessage)
        } else {
            for (player in players){
                player.sendMessage(message)
            }
        }
    }

    private fun assembleMessage(message: String, player : Player) : String{

    }

    private fun getPlayersInRange(sender: Player) : List<Player>{

    }
}