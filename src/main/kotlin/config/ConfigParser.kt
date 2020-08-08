package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit

class ConfigParser(private val main: Main) {
    lateinit var chats: List<String>
    lateinit var chatProperties : Map<String, Map<String, Any>>
    lateinit var defaultChannel : String

    init {
        read()
    }

    private fun read(){
        chats = main.config.getList("Channels") as List<String>

        for (chat in chats){
            val properties = emptyMap<String, Any>().toMutableMap()

            for (key in main.config.getConfigurationSection(chat)!!.getKeys(false)){
                properties[key] = main.config.getConfigurationSection(chat)!![key] as Any
            }

            chatProperties = mapOf(Pair(chat, properties))
        }

        if (main.config.getString("defaultChannel") != null) {
            defaultChannel = main.config.getString("defaultChannel")!!

        }else {
            Bukkit.getLogger().warning("You did not define a default channel")
        }
    }

}