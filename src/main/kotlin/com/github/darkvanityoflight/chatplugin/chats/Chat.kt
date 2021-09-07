package com.github.darkvanityoflight.chatplugin.chats

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.ChatColor
import org.bukkit.entity.Player


abstract class Chat : IChat {

    override fun assembleMessage(message: String, sender: Player) : String {
        var form = format
        var message : String = message


        if(main.papiEnabled){
            form = PlaceholderAPI.setPlaceholders(sender, form)
            if (sender.hasPermission("chatplugin.papi")){
                message = PlaceholderAPI.setPlaceholders(sender, message)
            }
        }

        if(sender.hasPermission("chatplugin.color")){
            form = ChatColor.translateAlternateColorCodes('&', form)
        }

        return form
    }

    override fun assembleMessage(message: String, sender: Player, form: String): String {
        var form = form
        var message = message


        if(main.papiEnabled){
            form = PlaceholderAPI.setPlaceholders(sender, form)
            if (sender.hasPermission("chatplugin.papi")){
                message = PlaceholderAPI.setPlaceholders(sender, message)
            }
        }

        if(sender.hasPermission("chatplugin.color")){
            form = ChatColor.translateAlternateColorCodes('&', form)
        }

        return form
    }
}
