package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.config.DataParser
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class SpyChat(val dataParser : DataParser) : CommandExecutor{

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if (sender.hasPermission("chatplugin.spy")){
            if (sender.name in dataParser.spyPlayer){
                dataParser.removeStringList(listOf(sender.name), "spy-players")
            }else{
                dataParser.addStringList(listOf<String>(sender.name), "spy-players")
            }

            dataParser.update()
        }

        return true
    }

}