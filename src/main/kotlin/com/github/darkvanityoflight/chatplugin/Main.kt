package com.github.darkvanityoflight.chatplugin

import com.github.darkvanityoflight.chatplugin.api.DataAPI
import com.github.darkvanityoflight.chatplugin.api.ParserAPI
import com.github.darkvanityoflight.chatplugin.chats.PlayerChat
import com.github.darkvanityoflight.chatplugin.chats.SpyChat
import com.github.darkvanityoflight.chatplugin.commands.ListChannels
import com.github.darkvanityoflight.chatplugin.commands.MuteChannel
import com.github.darkvanityoflight.chatplugin.commands.SwitchChannel
import com.github.darkvanityoflight.chatplugin.commands.UnmuteChannel
import com.github.darkvanityoflight.chatplugin.listeners.ChatListener
import com.github.darkvanityoflight.chatplugin.listeners.PlayerJoinListener
import com.github.darkvanityoflight.chatplugin.listeners.ServerLoadListener
import com.github.darkvanityoflight.chatplugin.parser.ConfigParser
import com.github.darkvanityoflight.chatplugin.parser.DataParser
import net.luckperms.api.LuckPerms
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.event.Listener
import java.io.File
import java.lang.reflect.Field
import com.github.darkvanityoflight.recraftedcore.ARecraftedPlugin
import com.onarandombox.MultiverseCore.MultiverseCore
import net.milkbowl.vault.economy.Economy
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.RegisteredServiceProvider


class Main : ARecraftedPlugin() {
    var configParser : ConfigParser = ConfigParser(config)
    val chats : MutableMap<String, PlayerChat> = emptyMap<String, PlayerChat>().toMutableMap()
    val dataParser : DataParser = DataParser(this)
    lateinit var spyChat: SpyChat
    var factionsEnabled: Boolean = false
    var luckPermsEnabled: Boolean = false
    var luckPermApi : LuckPerms? = null
    var multiversePlugin : MultiverseCore? = null
    var multiverseEnabled : Boolean = false
    var vaultEnabled : Boolean = false
    var economy : Economy? = null
    var papi : Plugin? = null
    var papiEnabled : Boolean = false

    override fun onEnable(){
        super.onEnable()
        saveDefaultConfig()
        configParser.read()


        // Check for apis/plugins to use
        if(Bukkit.getPluginManager().isPluginEnabled("MultiverseCore")){
            val plugin : Plugin = Bukkit.getServer().pluginManager.getPlugin("MultiverseCore")!!
            if (plugin is MultiverseCore) {
                multiversePlugin = plugin
                multiverseEnabled = true
            }else {
                severe("Multiverse seems to be enabled on your server, but we couldn't find it")
            }
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Factions")) factionsEnabled = true

        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")){
            luckPermsEnabled = true

            val luckPermProvider = Bukkit.getServicesManager().getRegistration(
                LuckPerms::class.java
            )
            if (luckPermProvider != null) {
                luckPermApi = luckPermProvider.provider
            }else{
                warning("Could not get the Lucky perm api even though lucky perms is enabled")
            }
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")){
            vaultEnabled = true
            val rsp : RegisteredServiceProvider<Economy>? =  server.servicesManager.getRegistration(Economy::class.java)
            if (rsp == null){
                vaultEnabled = false
                return
            }
            economy = rsp.provider
        }

        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")){
            papi = Bukkit.getPluginManager().getPlugin("PlaceholderAPI")
            if(papi == null){
                warning("PlaceholderAPI is enabled, but we could not find the plugin")
                papiEnabled = false
            }else {
                papiEnabled = true
            }
        }

        // Check if data file exists if not create
        val f = File(dataFolder.absolutePath + "/data.yml")
        if (!f.exists()){
            f.createNewFile()
        }
        dataParser.update()

        spyChat = SpyChat(this)

        val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap

        // Create chat obj for every chat defined in the config file
        for (channel in configParser.chats) {
            val properties = configParser.chatProperties[channel]
            if (properties != null) {
                if ("ignore_world" in properties.keys){
                    chats[channel.toUpperCase()] = PlayerChat(
                        properties["name"] as String, properties["list_style"] as String,
                        properties["ignore_world"] as Boolean, properties["format"] as String,
                        properties["muteable"] as Boolean, properties["radius"] as Int, this, channel, properties["msgCost"] as Int?
                    )
                } else{
                    chats[channel.toUpperCase()] = PlayerChat(
                        properties["name"] as String, properties["list_style"] as String,
                        properties["format"] as String, properties["muteable"] as Boolean,
                        properties["radius"] as Int, this, channel, properties["msgCost"] as Int?
                    )
                }
                if (!configParser.overwrite!!){
                    // Register our com.github.darkvanity.chatplugin.commands without plugin.yml
                    commandMap.register(
                        channel, SwitchChannel(
                            channel, properties["list_style"] as String, "/$channel",
                            "chatplugin.switchChannel", ArrayList(), this
                        )
                    )
                }
            }else {
                warning("No properties ofr $channel, please specify some")
            }
        }

        Bukkit.getPluginManager().registerEvents(PlayerJoinListener(dataParser, configParser), this)
        Bukkit.getPluginManager().registerEvents(ChatListener(this), this)
        this.getCommand("spy")?.setExecutor(spyChat)
        this.getCommand("mutech")?.setExecutor(MuteChannel(this))
        this.getCommand("unmutech")?.setExecutor(UnmuteChannel(this))

        val chatList = Array<PlayerChat?>(chats.keys.size) { null }
        chats.keys.forEachIndexed { pos, key -> chatList[pos] = chats[key]!! }
        val nonNullChats = Array<PlayerChat>(chats.keys.size) { pos -> chatList[pos]!! }
        this.getCommand("list_channels")?.setExecutor(ListChannels(nonNullChats))

        // Start a task to register our com.github.darkvanity.chatplugin.commands if overwrite is true
        if (configParser.overwrite!!) {
            Bukkit.getPluginManager().registerEvents(ServerLoadListener(this), this)
        }

        initalizeApis()
    }

    private fun initalizeApis(){
        ParserAPI.configParser = configParser
        ParserAPI.dataParser = dataParser
        ParserAPI.initialized = true

        DataAPI.channels = chats
        DataAPI.initialized = true
    }


}
