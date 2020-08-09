package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DataParser(main: Main) {
    var dataFile : File = File(main.dataFolder.toString() + "/data.yml")
    var playerChannelMap : Map<String, String> = emptyMap<String, String>().toMutableMap()

    fun updatePlayerChannelMap(){
        playerChannelMap = readDataMap("Player-Channels")
    }

    private fun readDataMap(key: String) : Map<String, String> {
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        val data : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
        val configSection : ConfigurationSection? = config.getConfigurationSection(key)

        if (configSection != null)
            for (dataKey : String in configSection.getKeys(false)){
                data[key] = configSection.getString(key + dataKey).toString()
            }

        return data
    }

    fun writeDataMap(data : Map<String, String>, key : String){
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        for (entry : String in data.keys) {
            config.set(key, data[key])
        }
        config.save(dataFile)
    }

    fun setData(key: String, value : Any){
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        config.set(key, value)
        config.save(dataFile)
    }
}