package com.github.DarkVanityOfLight.ChattPlugin

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin(), Listener, CommandExecutor{

    override fun onEnable(){

        val f : File = File(dataFolder.toString() + "data.yml")
        if (!f.exists()){
            val isCreated : Boolean = f.createNewFile()
            if (!isCreated) Bukkit.getLogger().warning("Could not create file ${dataFolder.toString()}data.yml")
        }
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onMessage(event: AsyncPlayerChatEvent){
        val msg = event.message
        event.isCancelled = true
    }
}