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
import sun.audio.AudioPlayer.player
import java.io.File
import java.lang.reflect.Field

class Main : JavaPlugin(), Listener, CommandExecutor{
    private val configParser : ConfigParser = ConfigParser(this)
    val chats : MutableMap<String, Chatt> = emptyMap<String, Chatt>().toMutableMap()
    lateinit var dataParser : DataParser

    override fun onEnable(){

        // Check if config file exists if not create
        var f = File("$dataFolder/config.yml")
        if (!f.exists()){
            val isCreated : Boolean = f.createNewFile()
            if (!isCreated) Bukkit.getLogger().warning("Could not create file ${dataFolder}/config.yml")
            Bukkit.getLogger().info("Config file for ChatPlugin is empty please define something in it and reload")
        }

        // Check if data file exists if not create
        f = File("$dataFolder/data.yml")
        if (!f.exists()){
            val isCreated : Boolean = f.createNewFile()
            if (!isCreated) Bukkit.getLogger().warning("Could not create file ${dataFolder}/data.yml")
        }
        dataParser = DataParser(this)

        // Create chat obj for every chat defined in the config file
        for (channel in configParser.chats) {
            val properties = configParser.chatProperties[channel]
            if (properties != null) {
                if ("ignoreWorld" in properties.keys){
                    chats[channel] = Chatt(
                            properties[name] as String, properties["list_style"] as String,
                            properties["ignoreWorld"] as Boolean, properties["format"] as String,
                            properties["muteable"] as Boolean, properties["radius"] as Int)
                }
                // Register our commands without plugin.yml
                try {
                    val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
                    bukkitCommandMap.setAccessible(true)
                    val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap
                    commandMap.register(channel, SwitchChannel(
                            channel, properties["list_style"] as String, "/$channel",
                            "chatPlugin.commands.switchChannel", ArrayList<String>(), this
                            ))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onMessage(event: AsyncPlayerChatEvent){
        dataParser.updatePlayerChannelMap()
        val channel = dataParser.playerChannelMap[event.player.name]
        val chat : Chatt? = chats[channel]
        if (chat == null){
            Bukkit.getLogger().warning("No channel with the name $channel could be found," +
                    " but was requested by player ${event.player.name}")
            event.isCancelled = true
            return
        }

        chat.sendMessage(event.message, event.player)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent){
        if (event.player.name !in dataParser.playerChannelMap.keys){
            dataParser.setData(player.name, configParser.defaultChannel)
        }
    }
}