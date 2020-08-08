package com.github.DarkVanityOfLight.ChattPlugin

import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin(), Listener, CommandExecutor{

    override fun onEnable(){
        Bukkit.getPluginManager().registerEvents(this, this)
    }

    @EventHandler
    fun onMessage(event: AsyncPlayerChatEvent){
        val msg = event.message
        event.isCancelled = true
    }
}