package com.github.darkvanityoflight.chatplugin.listeners


import com.github.darkvanityoflight.chatplugin.Main
import com.github.darkvanityoflight.chatplugin.commands.SwitchChannel
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerLoadEvent
import java.lang.reflect.Field

class ServerLoadListener(private val main : Main) : Listener {

    @EventHandler()
    fun onServerLoad(event : ServerLoadEvent){
        val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap

        // Register our com.github.darkvanity.chatplugin.commands without plugin.yml
        for (channel in main.configParser.chats){
            val properties = main.configParser.chatProperties[channel]

            commandMap.register(
                channel, SwitchChannel(
                    channel, properties!!["list_style"] as String, "/$channel",
                    "chatPlugin.com.github.darkvanity.chatplugin.commands.switchChannel", ArrayList(), main
                )
            )
        }
    }
}