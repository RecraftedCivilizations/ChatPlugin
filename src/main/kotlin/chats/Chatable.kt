package com.github.DarkVanityOfLight.ChattPlugin.chats

import org.bukkit.ChatColor
import org.bukkit.entity.Player

interface Chatable {
    var format : String

    fun sendMessage(message : String, sender : Player, channelName: String? = null)
    fun assembleMessage(message: String, sender : Player, channelName: String? = null) : String
}