package com.github.DarkVanityOfLight.ChattPlugin.config

import com.github.DarkVanityOfLight.ChattPlugin.Main
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class DataParser(main: Main) {
    var dataFile : File = File(main.dataFolder.absolutePath + "/data.yml")
    var playerChannelMap : MutableMap<String, String> = emptyMap<String, String>().toMutableMap()
    var spyPlayer : MutableSet<String> = emptySet<String>().toMutableSet()

    fun update(){
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)

        if (config.getConfigurationSection("Player-Channels") != null) {
            for (key in config.getConfigurationSection("Player-Channels")!!.getKeys(false)) {
                playerChannelMap[key] = config.getString("Player-Channels.${key}").toString()
            }
        }

        spyPlayer = emptySet<String>().toMutableSet()
        if (config.getConfigurationSection("spy-players") != null) {
            for (player in config.getConfigurationSection("spy-players")!!.getStringList("spy-players")){
                spyPlayer.add(player)
            }
        }

    }

    @Deprecated("Use the individual update function instead")
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

    fun getBoolean(key: String) : Boolean{
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        return config.getBoolean(key)
    }

    fun getStringList(key: String) : List<String>{
        val list = emptyList<String>().toMutableList()
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)

        return config.getStringList(key)
    }

    fun addStringList(toWrite: List<String>, key: String){
        val config : YamlConfiguration = YamlConfiguration.loadConfiguration(dataFile)
        val currentList = config.getStringList(key).toMutableList()

        currentList.addAll(toWrite)

        config.set(key, currentList)
    }
}