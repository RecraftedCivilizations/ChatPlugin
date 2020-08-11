package com.github.DarkVanityOfLight.ChattPlugin

import com.github.DarkVanityOfLight.ChattPlugin.commands.SwitchChannel
import com.github.DarkVanityOfLight.ChattPlugin.config.ConfigParser
import com.github.DarkVanityOfLight.ChattPlugin.config.DataParser
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.reflect.Field

class Main : JavaPlugin(), Listener, CommandExecutor{
    val configParser : ConfigParser = ConfigParser(this)
    val chats : MutableMap<String, Chat> = emptyMap<String, Chat>().toMutableMap()
    val dataParser : DataParser = DataParser(this)
    private val chatLog : File = File(this.dataFolder.absolutePath + "/log.lst")

    override fun onEnable(){

        if (!chatLog.exists()){
            chatLog.createNewFile()
        }

        configParser.read()

        // Check if data file exists if not create
        val f = File(dataFolder.absolutePath + "/data.yml")
        if (!f.exists()){
            f.createNewFile()
        }
        dataParser.updatePlayerChannelMap()

        val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap

        // Create chat obj for every chat defined in the config file
        for (channel in configParser.chats) {
            val properties = configParser.chatProperties[channel]
            if (properties != null) {
                if ("ignoreWorld" in properties.keys){
                    chats[channel] = Chat(
                            properties["name"] as String, properties["list_style"] as String,
                            properties["ignoreWorld"] as Boolean, properties["format"] as String,
                            properties["muteable"] as Boolean, properties["radius"] as Int)
                } else{
                    chats[channel] = Chat(
                            properties["name"] as String, properties["list_style"] as String,
                            properties["format"] as String, properties["muteable"] as Boolean,
                            properties["radius"] as Int)
                }
                // Register our commands without plugin.yml
                commandMap.register(channel, SwitchChannel(
                        channel, properties["list_style"] as String, "/$channel",
                        "chatPlugin.commands.switchChannel", ArrayList(), this
                ))
            }else {
                Bukkit.getLogger().warning("No properties ofr $channel, please specify some")
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onMessage(event: AsyncPlayerChatEvent){
        dataParser.updatePlayerChannelMap()
        val channel = dataParser.playerChannelMap[event.player.name]
        val chat : Chat? = chats[channel]
        if (chat == null){
            Bukkit.getLogger().warning("No channel with the name $channel could be found," +
                    " but was requested by player ${event.player.name}")
            event.isCancelled = true
            return
        }

        // Set the recipients to players in the radius
        val players = chat.getPlayersInRange(event.player)
        for (player in Bukkit.getOnlinePlayers()){
            if (player !in players){
                event.recipients.remove(player)
            }
        }
        val message = chat.assembleMessage(event.message, event.player)
        event.format = message

        log(message)

    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent){
        if (event.player.name !in dataParser.playerChannelMap.keys){
            dataParser.setData("Player-Channels.${event.player.name}", configParser.defaultChannel)
        }
    }

    fun log(message : String){
        chatLog.appendText(message + "\n")
    }
}
