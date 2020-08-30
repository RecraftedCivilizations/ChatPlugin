package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.github.DarkVanityOfLight.ChattPlugin.Main
import com.massivecraft.factions.FPlayers
import com.massivecraft.factions.FactionsPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

abstract class Chat : Chatable {

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


        } else {
            Bukkit.getLogger().info("Factions plugin is not enabled on this server you cannot use:\n" +
                    "%role_prefix%, %role%, %faction_tag%, %title%, %deaths%")
        }

        form = form.replace("%player_name%", sender.name)
        form = form.replace("%message%", message)
        form = form.replace("%nickname%", sender.displayName)
        form = form.replace("%channel_name%", channelName)

        form = ChatColor.translateAlternateColorCodes('&', form)
        return form
    }
}