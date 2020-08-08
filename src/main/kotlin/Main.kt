package com.github.DarkVanityOfLight.ChattPlugin

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
    private var chats : MutableList<Chatt> = mutableListOf()


    override fun onEnable(){
        saveDefaultConfig()
        val f = File(dataFolder.toString() + "data.yml")
        if (!f.exists()){
            val isCreated : Boolean = f.createNewFile()
            if (!isCreated) Bukkit.getLogger().warning("Could not create file ${dataFolder}data.yml")
        }

        // Create chat obj for every chat defined in the config file
        for ((index, channel) in parser.chats.withIndex()) {
            val properties = parser.chatProperties[channel]
            if (properties != null) {
                if ("ignoreWorld" in properties.keys){
                    chats[index] = Chatt(
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
        val msg = event.message
        event.isCancelled = true
    }
}