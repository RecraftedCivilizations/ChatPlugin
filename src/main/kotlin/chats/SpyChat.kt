package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.parser.ConfigParser
import com.github.DarkVanityOfLight.ChattPlugin.parser.DataParser
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpyChat(val dataParser : DataParser, val configParser: ConfigParser) : CommandExecutor, Chat() {
    override var format: String = configParser.spyFormat!!


    override fun sendMessage(message : String, sender : Player, channelName: String?) {
        val spyPlayer = dataParser.spyPlayer
        val sendToPlayers = emptySet<Player>().toMutableSet()

        val asmMessage = assembleMessage(message, sender, channelName)

        Bukkit.getOnlinePlayers().forEach{ player -> if (player.name in spyPlayer) sendToPlayers.add(player)}

        sendToPlayers.forEach { spy -> spy.sendMessage(asmMessage) }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player){
            if (sender.hasPermission("chatplugin.spy")){
                if (sender.name in dataParser.spyPlayer){
                    dataParser.removeStringList(listOf(sender.name), "spy-players")
                }else{
                    dataParser.addStringList(listOf<String>(sender.name), "spy-players")
                }

                dataParser.update()
            }

            return true
        }else {
            Bukkit.getLogger().info("You must be a player to use this command")
            return false
        }
    }

}