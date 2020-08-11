package com.github.DarkVanityOfLight.ChattPlugin.commands

import com.github.DarkVanityOfLight.ChattPlugin.config.DataParser
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import sun.applet.Main

class SpyChat(val dataParser : DataParser) : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender.hasPermission("chatplugin.spy")){
            val current = dataParser.getBoolean("spy-mode.${sender.name}")
            dataParser.setData("spy-mode.${sender.name}", !current)
        }

        return true
    }


}