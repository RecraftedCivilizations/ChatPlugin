package com.github.DarkVanityOfLight.ChattPlugin.chats

import org.bukkit.ChatColor
import org.bukkit.entity.Player

abstract class Chat : Chatable {

    override fun assembleMessage(message: String, sender : Player) : String {
        var form = format

        form = form.replace("%player_name%", sender.name)
        form = form.replace("%message%", message)
        form = form.replace("%nickname%", sender.displayName)

        form = ChatColor.translateAlternateColorCodes('&', form)
        return form
    }
}