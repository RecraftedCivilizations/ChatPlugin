package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

class ConfigParser(private val main: Main) {
    lateinit var chats: List<String>
    lateinit var chatProperties : Map<String, Map<String, Any>>
    lateinit var defaultChannel : String
    lateinit var config : FileConfiguration

    fun read(){
        main.saveDefaultConfig()
        main.reloadConfig()

        config = main.getConfig()

        chats = config.getList("Channels") as List<String>

        for (chat in chats){
            val properties = emptyMap<String, Any>().toMutableMap()

            for (key in config.getConfigurationSection(chat)!!.getKeys(false)){
                properties[key] = config.getConfigurationSection(chat)!![key] as Any
            }

            chatProperties = mapOf(Pair(chat, properties))
        }

        if (config.getString("defaultChannel") != null) {
            defaultChannel = config.getString("defaultChannel")!!

        }else {
            Bukkit.getLogger().warning("You did not define a default channel")
        }
    }

}