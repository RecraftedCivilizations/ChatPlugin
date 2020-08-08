package com.github.DarkVanityOfLight.ChattPlugin

import org.bukkit.entity.Player

class Chatt {
    lateinit var format: String;

    constructor(name : String, list_style : String, ignore_world : Boolean, format : String, muteable : Boolean){
        this.format = format
    }
    constructor(name: String, list_style: String, format: String, muteable: Boolean){}

    fun send_message(message : String, sender : Player){
        var players : List<Player> = get_players_in_range(sender)

        for (player in players){
            player.sendMessage(message)
        }
    }

    fun assemble_message(message: String, player : Player){

    }

    fun get_players_in_range(sender: Player) : List<Player>{

    }
}