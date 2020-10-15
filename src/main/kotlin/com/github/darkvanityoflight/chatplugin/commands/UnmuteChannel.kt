package com.github.darkvanityoflight.chatplugin.commands

import com.github.darkvanityoflight.chatplugin.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class UnmuteChannel(val main: Main): CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player){
        main.dataParser.removeStringList(args.toList(), "Mutet-Channels.${sender.name}")
        return true
        }else{
            sender.sendMessage("You can't use that from the console")
            return false
        }
    }


}