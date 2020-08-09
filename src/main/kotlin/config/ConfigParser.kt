package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

class ConfigParser(private val main: Main) {
    lateinit var chats: List<String>
    lateinit var chatProperties : MutableMap<String, Map<String, Any>>
    lateinit var defaultChannel : String
    lateinit var config : FileConfiguration

    fun read(){
        main.saveDefaultConfig()
        main.reloadConfig()

        chatProperties = emptyMap<String, Map<String, Any>>().toMutableMap()

        config = main.getConfig()

        chats =  config.getConfigurationSection("Channels")?.getKeys(false)!!.toList()

        for (chat in chats){
            val properties = emptyMap<String, Any>().toMutableMap()
            for (key in config.getConfigurationSection("Channels.$chat")!!.getKeys(false)){
                properties[key] = config.getConfigurationSection("Channels.$chat")!![key] as Any
            }

            chatProperties[chat] = properties
        }

        if (config.getString("defaultChannel") != null) {
            defaultChannel = config.getString("defaultChannel")!!

        }else {
            Bukkit.getLogger().warning("You did not define a default channel")
        }
    }

}