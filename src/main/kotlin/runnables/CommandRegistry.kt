package com.github.DarkVanityOfLight.ChattPlugin.runnables

import com.github.DarkVanityOfLight.ChattPlugin.Main
import com.github.DarkVanityOfLight.ChattPlugin.commands.SwitchChannel
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.scheduler.BukkitRunnable
import java.lang.reflect.Field

class CommandRegistry(val main : Main) : BukkitRunnable() {

    override fun run() {
        val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap

        // Register our commands without plugin.yml
        for (channel in main.configParser.chats){
            val properties = main.configParser.chatProperties[channel]

            commandMap.register(
                channel, SwitchChannel(
                    channel, properties!!["list_style"] as String, "/$channel",
                    "chatPlugin.commands.switchChannel", ArrayList(), main
                )
            )
        }
    }
}