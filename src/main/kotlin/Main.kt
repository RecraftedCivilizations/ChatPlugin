package com.github.DarkVanityOfLight.ChattPlugin

import com.github.DarkVanityOfLight.ChattPlugin.chats.PlayerChat
import com.github.DarkVanityOfLight.ChattPlugin.chats.SpyChat
import com.github.DarkVanityOfLight.ChattPlugin.commands.SwitchChannel
import com.github.DarkVanityOfLight.ChattPlugin.parser.ConfigParser
import com.github.DarkVanityOfLight.ChattPlugin.parser.DataParser
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.lang.reflect.Field

class Main : JavaPlugin(), Listener, CommandExecutor{
    val configParser : ConfigParser = ConfigParser(this)
    val chats : MutableMap<String, PlayerChat> = emptyMap<String, PlayerChat>().toMutableMap()
    val dataParser : DataParser = DataParser(this)
    private val chatLog : File = File(this.dataFolder.absolutePath + "/log.lst")
    private lateinit var spyChat: SpyChat

    override fun onEnable(){

        if (!chatLog.exists()){
            chatLog.createNewFile()
        }

        configParser.read()

        // Check if data file exists if not create
        val f = File(dataFolder.absolutePath + "/data.yml")
        if (!f.exists()){
            f.createNewFile()
        }
        dataParser.update()

        spyChat = SpyChat(dataParser, configParser)

        val bukkitCommandMap: Field = Bukkit.getServer().javaClass.getDeclaredField("commandMap")
        bukkitCommandMap.isAccessible = true
        val commandMap: CommandMap = bukkitCommandMap.get(Bukkit.getServer()) as CommandMap

        // Create chat obj for every chat defined in the config file
        for (channel in configParser.chats) {
            val properties = configParser.chatProperties[channel]
            if (properties != null) {
                if ("ignoreWorld" in properties.keys){
                    chats[channel] = PlayerChat(
                            properties["name"] as String, properties["list_style"] as String,
                            properties["ignoreWorld"] as Boolean, properties["format"] as String,
                            properties["muteable"] as Boolean, properties["radius"] as Int)
                } else{
                    chats[channel] = PlayerChat(
                            properties["name"] as String, properties["list_style"] as String,
                            properties["format"] as String, properties["muteable"] as Boolean,
                            properties["radius"] as Int)
                }
                // Register our commands without plugin.yml
                commandMap.register(channel, SwitchChannel(
                        channel, properties["list_style"] as String, "/$channel",
                        "chatPlugin.commands.switchChannel", ArrayList(), this
                ))
            }else {
                Bukkit.getLogger().warning("No properties ofr $channel, please specify some")
            }
        }

        Bukkit.getPluginManager().registerEvents(this, this)
        this.getCommand("spy")?.setExecutor(spyChat)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onMessage(event: AsyncPlayerChatEvent){
        dataParser.update()
        val channel = dataParser.playerChannelMap[event.player.name]
        val chat : PlayerChat? = chats[channel]
        if (chat == null){
            Bukkit.getLogger().warning("No channel with the name $channel could be found," +
                    " but was requested by player ${event.player.name}")
            event.isCancelled = true
            return
        }

        // Set the recipients to players in the radius
        if (!chat.ignoreWorld){
            val players = chat.getPlayersInRange(event.player)
            val recipients = emptySet<Player>().toMutableSet()
            Bukkit.getOnlinePlayers().forEach{player ->  if (player in players) recipients.add(player)}
            event.recipients.removeAll(event.recipients)
            event.recipients.addAll(recipients)
        }

        val message = chat.assembleMessage(event.message, event.player)
        event.format = message

        spyChat.sendMessage(event.message, event.player, channel)
        log(message)

    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent){
        if (event.player.name !in dataParser.playerChannelMap.keys){
            dataParser.setData("Player-Channels.${event.player.name}", configParser.defaultChannel)
        }
    }

    fun log(message : String){
        chatLog.appendText(message + "\n")
    }
}
