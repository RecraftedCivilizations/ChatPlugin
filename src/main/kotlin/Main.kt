package com.github.DarkVanityOfLight.ChattPlugin

import com.github.DarkVanityOfLight.ChattPlugin.config.DataParser
import com.github.DarkVanityOfLight.ChattPlugin.config.configParser
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin(), Listener, CommandExecutor{
    private val parser : configParser = configParser(this)
    private val chats : MutableMap<String, Chatt> = emptyMap<String, Chatt>().toMutableMap()
    private lateinit var dataParser : DataParser


    override fun onEnable(){
        saveDefaultConfig()
        val f = File(dataFolder.toString() + "data.yml")
        if (!f.exists()){
            val isCreated : Boolean = f.createNewFile()
            if (!isCreated) Bukkit.getLogger().warning("Could not create file ${dataFolder}data.yml")
        }
        dataParser = DataParser(this)

        // Create chat obj for every chat defined in the config file
        for (channel in parser.chats) {
            val properties = parser.chatProperties[channel]
            if (properties != null) {
                if ("ignoreWorld" in properties.keys){
                    chats[channel] = Chatt(
                            properties[name] as String, properties["list_style"] as String,
                            properties["ignoreWorld"] as Boolean, properties["format"] as String,
                            properties["muteable"] as Boolean, properties["radius"] as Int)
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
}