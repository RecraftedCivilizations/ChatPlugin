package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.ChatColor
import org.bukkit.entity.Player

interface IChat {
    var format : String
    var main: Main
    var channelName: String

    fun sendMessage(message : String, sender : Player)
    fun assembleMessage(message: String, sender : Player) : String
}