package com.github.DarkVanityOfLight.ChattPlugin.commands

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SwitchChannel(private val main: Main) : CommandExecutor {

    @Override
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player){
            if (args[0] in main.chats.keys){
                main.dataParser.setData(sender.name, args[0])
            } else {
                Bukkit.getLogger().info("Player ${sender.name} tried to switch to channel ${args[0]} but the channel does not exist")
            }


        }
        return true
    }
}