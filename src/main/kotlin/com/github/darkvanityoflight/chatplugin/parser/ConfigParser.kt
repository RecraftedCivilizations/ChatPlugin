package com.github.darkvanityoflight.chatplugin.parser


import org.bukkit.Bukkit
import com.github.darkvanityoflight.recraftedcore.configparser.ARecraftedConfigParser
import org.bukkit.configuration.file.FileConfiguration

class ConfigParser(config: FileConfiguration) : ARecraftedConfigParser(config) {
    lateinit var chats: List<String>
    lateinit var chatProperties : MutableMap<String, Map<String, Any>>
    lateinit var defaultChannel : String
    lateinit var swearWords: List<String>
    var overwrite : Boolean? = null
    var spyFormat : String? = null

    override fun read(){
        val swearWordsList = emptyList<String>().toMutableList()
        swearWordsList.plus(config.getStringList("Swearwords"))
        swearWords = swearWordsList

        chatProperties = emptyMap<String, Map<String, Any>>().toMutableMap()


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
            //Loop throught all keys in the config section and add it to the chat map
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