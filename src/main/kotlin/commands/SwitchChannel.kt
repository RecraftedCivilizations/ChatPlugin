package com.github.DarkVanityOfLight.ChattPlugin.commands

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class SwitchChannel(private val channelName: String, description: String, usage: String, permission: String,
                    aliases: ArrayList<String>, private val main: Main) : BukkitCommand(channelName) {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender is Player){
                if (args.isEmpty()) {
                    main.dataParser.setData("Player-Channels.${sender.name}", channelName)
                    main.chats[channelName]?.name?.let { sender.sendMessage(it) }
                }else {
                    val result = args.joinToString(" ")
                    main.chats[channelName]?.sendMessage(result, sender)
                }

        } else {
            Bukkit.getLogger().info("A non player entity tried sending a command wtf")
            return false
        }
        return true
    }
}

