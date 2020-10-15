package com.github.darkvanityoflight.chatplugin.chats

import com.github.darkvanityoflight.chatplugin.Main
import org.bukkit.entity.Player

interface IChat {
    var format : String
    var main: Main
    var channelName: String

    fun sendMessage(message : String, sender : Player)
    fun sendMessage(message: String, sender: Player, form : String)
    fun assembleMessage(message: String, sender : Player) : String
    fun assembleMessage(message: String, sender: Player, form: String) : String
}