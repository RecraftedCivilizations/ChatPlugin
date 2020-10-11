package com.github.DarkVanityOfLight.ChattPlugin.chats

import com.massivecraft.factions.FPlayers
import net.luckperms.api.model.user.User
import net.luckperms.api.query.QueryOptions
import org.bukkit.ChatColor
import org.bukkit.entity.Player


abstract class Chat : IChat {

    override fun assembleMessage(message: String, sender: Player) : String {
        var form = format
        var message : String = message

        if (main.factionsEnabled){
            val fPlayer = FPlayers.getInstance().getByPlayer(sender)


            val rolePrefix = fPlayer.rolePrefix
            val role = fPlayer.role.name
            val factionTag = fPlayer.faction.tag
            val title = fPlayer.title
            val deaths = fPlayer.deaths.toString()

            form = form.replace("{role_prefix}", rolePrefix)
            form = form.replace("{role}", role)
            form = form.replace("{faction_tag}", factionTag)
            form = form.replace("{title}", title)
            form = form.replace("{deaths}", deaths)

            message = message.replace("{role_prefix}", rolePrefix)
            message = message.replace("{role}", role)
            message = message.replace("{faction_tag}", factionTag)
            message = message.replace("{title}", title)
            message = message.replace("{deaths}", deaths)


        }

        if (main.luckPermsEnabled){
            val user : User? = main.luckPermApi!!.userManager.getUser(sender.uniqueId)
            if (user != null) {
                val queryOptions: QueryOptions = main.luckPermApi!!.contextManager.getQueryOptions(sender)
                val metaData = user.cachedData.getMetaData(queryOptions)

                val prefix: String? = metaData.prefix
                form = if (prefix != null){
                    form.replace("{prefix}", prefix)
                }else {
                    form.replace("{prefix}", "")
                }
            }
        }

        form = form.replace("{player_name}", sender.name)
        form = form.replace("{message}", message)
        form = form.replace("{nickname}", sender.displayName)
        form = form.replace("{channel_name}", channelName)

        form = ChatColor.translateAlternateColorCodes('&', form)
        return form
    }
}
