package com.github.darkvanityoflight.chatplugin.chats

import com.github.darkvanityoflight.chatplugin.Main
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpyChat(override var main: Main) : CommandExecutor, Chat() {
    override var format: String = main.configParser.spyFormat!!
    override var channelName: String = "SpyChat"


    override fun sendMessage(message : String, sender : Player) {
        val spyPlayer = main.dataParser.spyPlayer
        val sendToPlayers = emptySet<Player>().toMutableSet()

        val asmMessage = assembleMessage(message, sender)

        Bukkit.getOnlinePlayers().forEach{ player -> if (player.name in spyPlayer) sendToPlayers.add(player)}

        sendToPlayers.forEach { spy -> spy.sendMessage(asmMessage) }
    }

    override fun sendMessage(message: String, sender: Player, form: String) {
        val spyPlayer = main.dataParser.spyPlayer
        val sendToPlayers = emptySet<Player>().toMutableSet()

        val asmMessage = assembleMessage(message, sender, form)

        Bukkit.getOnlinePlayers().forEach{ player -> if (player.name in spyPlayer) sendToPlayers.add(player)}

        sendToPlayers.forEach { spy -> spy.sendMessage(asmMessage) }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player){
            if (sender.hasPermission("chatplugin.spy")){
                if (sender.name in main.dataParser.spyPlayer){
                    main.dataParser.removeStringList(listOf(sender.name), "spy-players")
                }else{
                    main.dataParser.addStringList(listOf<String>(sender.name), "spy-players")
                }

                main.dataParser.update()
            }

            return true
        }else {
            Bukkit.getLogger().info("You must be a player to use this command")
            return false
        }
    }

}