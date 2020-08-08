package com.github.DarkVanityOfLight.ChattPlugin.commands

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class SwitchChannel(private var channelName: String, description: String, usage: String, permission: String,
                    aliases: ArrayList<String>, private var main: Main) : BukkitCommand(channelName) {

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender is Player){
                main.dataParser.setData(sender.name, channelName)
        } else {
            Bukkit.getLogger().info("A non player entity tried sending a command wtf")
            return false
        }
        return true
    }
}

