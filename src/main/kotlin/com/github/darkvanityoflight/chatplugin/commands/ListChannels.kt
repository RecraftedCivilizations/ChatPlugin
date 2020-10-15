package com.github.darkvanityoflight.chatplugin.commands


import com.github.darkvanityoflight.chatplugin.chats.PlayerChat
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ListChannels(val channels: Array<PlayerChat>) : CommandExecutor {
    private lateinit var formatedChannels : Array<String>

    init {
        formatedChannels = Array(channels.size) { pos -> channels[pos].listStyle }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        sender.sendMessage("These are all channels:")

        formatedChannels.forEach { channel -> sender.sendMessage(channel) }
        return true
    }


}