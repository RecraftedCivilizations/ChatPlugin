package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.massivecraft.factions.FPlayers
import net.luckperms.api.cacheddata.CachedMetaData
import net.luckperms.api.model.user.User
import net.luckperms.api.query.QueryOptions
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.util.io.BukkitObjectInputStream

abstract class Chat : IChat {

    override fun assembleMessage(message: String, sender : Player) : String {
        var form = format

        if (main.factionsEnabled){
            val fPlayer = FPlayers.getInstance().getByPlayer(sender)


            val rolePrefix = fPlayer.rolePrefix
            val role = fPlayer.role.name
            val factionTag = fPlayer.faction.tag
            val title = fPlayer.title
            val deaths = fPlayer.deaths.toString()

            form.replace("%role_prefix%", rolePrefix)
            form.replace("%role%", role)
            form.replace("%faction_tag%", factionTag)
            form.replace("%title%", title)
            form.replace("%deaths%", deaths)

            message.replace("%role_prefix%", rolePrefix)
            message.replace("%role%", role)
            message.replace("%faction_tag%", factionTag)
            message.replace("%title%", title)
            message.replace("%deaths%", deaths)


        }

        if (main.luckPermsEnabled){
            val user : User? = main.luckPermApi!!.userManager.getUser(sender.uniqueId)
            if (user != null) {
                val metadata : CachedMetaData = user.cachedData.metaData
                if (metadata.prefix != null){
                    form.replace("%prefix%", metadata.prefix!!)
                }else{
                    form.replace("%prefix%", "")
                    Bukkit.getLogger().warning("No prefix found for player ${sender.name}")
                }
                if (metadata.suffix != null){
                    form.replace("%suffix%", metadata.suffix!!)
                }else {
                    Bukkit.getLogger().warning("No suffix found for player ${sender.name}")
                }
            }
        }

        form = form.replace("%player_name%", sender.name)
        form = form.replace("%message%", message)
        form = form.replace("%nickname%", sender.displayName)
        form = form.replace("%channel_name%", channelName)

        form = ChatColor.translateAlternateColorCodes('&', form)
        return form
    }
}