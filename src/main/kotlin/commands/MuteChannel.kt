package com.github.DarkVanityOfLight.ChattPlugin.commands

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MuteChannel(val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player){
            val muteable = emptyList<String>().toMutableList()


            args.forEach{ it ->
                val uppercaseIt = it.toUpperCase()
                if (main.chats[uppercaseIt] != null) {
                    if (main.chats[uppercaseIt]!!.muteable){
                        muteable.add(main.chats[uppercaseIt]!!.channelName)
                        main.debug_log(main.chats[uppercaseIt]!!.channelName)
                    }else {
                        sender.sendMessage("Channel $uppercaseIt is not muteable")
                    }
                }else{
                    sender.sendMessage("Channel $uppercaseIt does not exist")
                }
            }

            main.dataParser.removeStringList(muteable, "Mutet-Channels.${sender.name}")
            main.dataParser.addStringList(muteable, "Mutet-Channels.${sender.name}")
            return true
        }else{
            sender.sendMessage("You can't use that as console")
            return false
        }
    }


}