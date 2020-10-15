package com.github.darkvanityoflight.chatplugin.chats

import com.github.darkvanityoflight.chatplugin.Main
import com.github.darkvanityoflight.chatplugin.chats.Chat
import com.massivecraft.factions.FPlayers
import com.massivecraft.factions.struct.ChatMode
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PlayerChat : Chat {
    private var radius : Int = 0
    var ignoreWorld = true
    var name : String = ""
    var muteable : Boolean = false
    var listStyle : String = ""
    override var main : Main
    override var channelName: String
    override var format: String

    constructor(name : String, list_style : String, ignoreWorld : Boolean, format : String, muteable : Boolean, radius : Int, main: Main, channelName : String){
        this.format = format
        this.ignoreWorld = ignoreWorld
        this.radius = radius
        this.main = main
        this.name = name
        this.muteable = muteable
        this.listStyle = list_style
        this.channelName = channelName
    }
    constructor(name: String, list_style: String, format: String, muteable: Boolean, radius: Int, main: Main, channelName : String){
        this.radius = radius
        this.format = format
        this.main = main
        this.name = name
        this.muteable = muteable
        this.listStyle = list_style
        this.channelName = channelName
    }

    override fun sendMessage(message : String, sender : Player){
        val players : List<Player> = getPlayersInRange(sender)
        val assembledMessage : String = assembleMessage(message, sender)

        for (player in players){
            player.sendMessage(assembledMessage)
        }

        if (main.factionsEnabled){
            if (FPlayers.getInstance().getByPlayer(sender).chatMode != ChatMode.PUBLIC || !ignoreWorld) {
                main.spyChat.sendMessage(message, sender)
            }
        } else if (!ignoreWorld){
            main.spyChat.sendMessage(message, sender)
        }

    }

    private fun getPlayersInRange(sender: Player) : List<Player>{
        val players = mutableListOf<Player>()

        if (this.ignoreWorld){
            players +=  Bukkit.getOnlinePlayers().toList()
        }else {
            for (player in Bukkit.getOnlinePlayers()){
                if (player.world == sender.world){
                if (player.location.distance(sender.location) <= this.radius){
                        players.add(player)
                    }
                }
            }
        }

        players.removeAll(getMutetPlayers())

        return players
    }

    fun getMutetPlayers() : Set<Player>{
        val mutePlayers = emptySet<Player>().toMutableSet()

        for (player in Bukkit.getOnlinePlayers()){
            if (channelName.toUpperCase() in main.dataParser.getStringList("Mutet-Channels.${player.name}")){
                mutePlayers.add(player)
            }
        }

        return mutePlayers
    }
}
