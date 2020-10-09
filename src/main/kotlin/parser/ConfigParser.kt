package com.github.DarkVanityOfLight.ChattPlugin.parser

import com.github.DarkVanityOfLight.ChattPlugin.Main
import com.github.darkvanityoflight.darkmodcore.configparser.ADarkModConfigParser
import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration

class ConfigParser(private val main: Main) : ADarkModConfigParser() {
    lateinit var chats: List<String>
    lateinit var chatProperties : MutableMap<String, Map<String, Any>>
    lateinit var defaultChannel : String
    lateinit var config : FileConfiguration
    var overwrite : Boolean? = null
    var spyFormat : String? = null

    override fun read(){
        main.saveDefaultConfig()
        main.reloadConfig()

        chatProperties = emptyMap<String, Map<String, Any>>().toMutableMap()

        config = main.config

        overwrite = config.getBoolean("overwrite")

        if (overwrite == null){
            overwrite = false
        }
        overwrite!!

        spyFormat = config.getString("spy-format")
        if (spyFormat == null){
            Bukkit.getLogger().warning("No default spy format found using: %channel% %sender% %message%")
            spyFormat = "%channel% %sender% %message%"
        }

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