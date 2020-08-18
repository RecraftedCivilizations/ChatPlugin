package com.github.DarkVanityOfLight.ChattPlugin.commands

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class MuteChannel(val main: Main) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        val args = args.toList().toMutableList()
        val muteable = emptyList<String>().toMutableList()

        args.forEach{
            if (main.chats[it]?.muteable != null) {
                if (main.chats[it]!!.muteable){
                    muteable.add(main.chats[it]!!.name)
                }else {
                    sender.sendMessage("Channel $it is not muteable")
                }
            }else{
                sender.sendMessage("Channel $it does not exist")
            }
        }

        main.dataParser.removeStringList(muteable, "Mutet-Channels.${sender.name}")
        main.dataParser.addStringList(muteable, "Mutet-Channels.${sender.name}")
        return true
    }


}