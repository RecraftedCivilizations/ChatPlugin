package com.github.DarkVanityOfLight.ChattPlugin.chats

import org.bukkit.ChatColor
import org.bukkit.entity.Player

interface Chatable {
    abstract var format : String

    fun sendMessage()
    fun assembleMessage(message: String, sender: Player, channelName: String? = null) : String
}