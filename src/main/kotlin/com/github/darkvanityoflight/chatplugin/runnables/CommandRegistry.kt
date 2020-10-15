package com.github.darkvanityoflight.chatplugin.runnables

import com.github.darkvanityoflight.chatplugin.Main
import com.github.darkvanityoflight.chatplugin.commands.SwitchChannel
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import java.lang.reflect.Field

@Deprecated("Use the command preprocess Event")
class CommandRegistry(val main : Main) : Runnable {

    override fun run() {
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