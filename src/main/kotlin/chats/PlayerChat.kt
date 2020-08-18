package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.Main
import com.massivecraft.factions.FPlayers
import com.massivecraft.factions.struct.ChatMode
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerChat : Chat{
    override var format : String
    private var radius : Int = 0
    var ignoreWorld = true
    var name : String = ""
    var muteable : Boolean = false
    override var main : Main

    constructor(name : String, list_style : String, ignoreWorld : Boolean, format : String, muteable : Boolean, radius : Int, main: Main){
        this.format = format
        this.ignoreWorld = ignoreWorld
        this.radius = radius
        this.main = main
        this.name = name
        this.muteable = muteable
    }
    constructor(name: String, list_style: String, format: String, muteable: Boolean, radius: Int, main: Main){
        this.radius = radius
        this.format = format
        this.main = main
        this.name = name
        this.muteable = muteable
    }

    override fun sendMessage(message : String, sender : Player, channelName: String?){
        val players : List<Player> = getPlayersInRange(sender)
        val assembledMessage : String = assembleMessage(message, sender)

        for (player in players){
            player.sendMessage(assembledMessage)
        }

        if (main.factionsEnabled){
            if (FPlayers.getInstance().getByPlayer(sender).chatMode != ChatMode.PUBLIC || !ignoreWorld) {
                main.spyChat.sendMessage(message, sender, name)
            }
        } else if (!ignoreWorld){
            main.spyChat.sendMessage(message, sender, name)
        }

    }

    private fun getPlayersInRange(sender: Player) : List<Player>{
        val players = mutableListOf<Player>()

        if (this.ignoreWorld){
            return Bukkit.getOnlinePlayers().toList()
        }

        for (player in Bukkit.getOnlinePlayers()){
            if (player.location.distance(sender.location) <= this.radius){
                if (name !in main.dataParser.getStringList("Mutet-Channels.${sender.name}"))
                    players.add(player)
            }
        }

        return players
    }
}