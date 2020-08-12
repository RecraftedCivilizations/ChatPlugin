package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.Main
import com.massivecraft.factions.FPlayers
import com.massivecraft.factions.FactionsPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

abstract class Chat : Chatable {

    override fun assembleMessage(message: String, sender : Player, channelName: String?) : String {
        var form = format

        if (Bukkit.getPluginManager().getPlugin("Factions") != null){
            val factionsPlugin = Bukkit.getPluginManager().getPlugin("Factions")!!.javaClass as FactionsPlugin
            val fPlayer = FPlayers.getInstance().getByPlayer(sender)


            val rolePrefix = fPlayer.rolePrefix
            val role = fPlayer.role.name
            val factionTag = fPlayer.faction.tag
            val title = fPlayer.title
            val deaths = fPlayer.deaths.toString()
            val discordUser = fPlayer.discordUser().name

            form.replace("%role_prefix%", rolePrefix)
            form.replace("%role%", role)
            form.replace("%faction_tag%", factionTag)
            form.replace("%title%", title)
            form.replace("%deaths%", deaths)
            form.replace("%discord_user%", discordUser)

            message.replace("%role_prefix%", rolePrefix)
            message.replace("%role%", role)
            message.replace("%faction_tag%", factionTag)
            message.replace("%title%", title)
            message.replace("%deaths%", deaths)
            message.replace("%discord_user%", discordUser)


        } else {
            Bukkit.getLogger().info("Factions plugin is not enabled on this server you cannot use:\n" +
                    "%role_prefix%, %role%, %faction_tag%, %title%, %deaths%, %discord_user%")
        }

        form = form.replace("%player_name%", sender.name)
        form = form.replace("%message%", message)
        form = form.replace("%nickname%", sender.displayName)

        if (channelName != null){
            form = form.replace("%channel_name%", channelName)
        }

        form = ChatColor.translateAlternateColorCodes('&', form)
        return form
    }
}