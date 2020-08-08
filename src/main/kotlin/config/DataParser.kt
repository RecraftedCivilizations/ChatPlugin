package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DataParser(main: Main) {
    var dataFile : File = File(main.dataFolder.toString() + "data.yml")

    fun readDataMap(key: String) : Map<String, String> {
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        val playerChatData : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        val configSection : ConfigurationSection? = config.getConfigurationSection(key)

        if (configSection != null)
            for (dataKey : String in configSection.getKeys(false)){
                playerChatData[key] = configSection.getString(key + dataKey).toString()
            }

        return playerChatData
    }

    fun writeDataMap(data : Map<String, String>, key : String){
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        for (entry : String in data.keys) {
            config.set(key, data[key])
        }
        config.save(dataFile)
    }
}