package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DataParser(main: Main) {
    var dataFile : File = File(main.dataFolder.toString() + "data.yml")

    fun readData() : Map<String, String> {
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        val playerChatData : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        val configSection : ConfigurationSection? = config.getConfigurationSection("PlayerChatData")

        if (configSection != null)
            for (key : String in configSection.getKeys(false)){
                playerChatData[key] = configSection.getString("PlayerChatData $key").toString()
            }

        return playerChatData
    }
}