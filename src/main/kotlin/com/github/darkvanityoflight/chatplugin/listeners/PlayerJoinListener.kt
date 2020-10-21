package com.github.darkvanityoflight.chatplugin.listeners

import com.github.darkvanityoflight.chatplugin.parser.ConfigParser
import com.github.darkvanityoflight.chatplugin.parser.DataParser
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(private val dataParser : DataParser, private val configParser: ConfigParser) : Listener {

    @EventHandler
    fun onJoin(event: PlayerJoinEvent){
        if (event.player.name !in dataParser.playerChannelMap.keys){
            dataParser.setData("Player-Channels.${event.player.name}", configParser.defaultChannel)
        }
    }
}