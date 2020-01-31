package net.evilblock.stark.profile

import net.evilblock.stark.Stark
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import net.minecraft.server.v1_7_R4.Block.p
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.lang.Exception

class ProfileListeners : Listener {

    @EventHandler
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        if (System.currentTimeMillis() - 1000 < Stark.instance.enabledAt) {
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            event.kickMessage = "${ChatColor.RED}This server is still loading..."
            return
        }

        // Need to check if player is still logged in when receiving another login attempt
        // This happens when a player using a custom client can access the server list while in-game (and reconnecting)
        val player = Bukkit.getPlayer(event.uniqueId)
        if (player != null && player.isOnline) {
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            event.kickMessage = "${ChatColor.RED}You tried to login too quickly after disconnecting.\nTry again in a few seconds."

            Stark.instance.server.scheduler.runTask(Stark.instance) {
                player.kickPlayer("${ChatColor.RED}Duplicate login kick")
            }

            return
        }

        // Search for an active ban
        //   - by uuid
        //   - by current ip address
        try {
            val punishment = Stark.instance.core.getProfileHandler().findActivePunishment(event.uniqueId, event.address.hostAddress)

            if (punishment != null) {
                var kickMessage = if (punishment.second.type == ProfilePunishmentType.BAN) {
                    "${ChatColor.RED}Your account has been banned from the Kihar Network.\nCreate a support ticket on our site to appeal."
                } else {
                    "${ChatColor.RED}Your account has been blacklisted from the Kihar Network.\nThis type of appeal can't be appealed."
                }

                if (event.uniqueId != punishment.first) {
                    kickMessage += "\nThis punishment is in relation to ${ChatColor.YELLOW}${Stark.instance.core.uuidCache.name(punishment.first)}${ChatColor.RED}."
                }

                event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
                event.kickMessage = kickMessage
            }
        } catch (e: Exception) {
            Stark.instance.logger.info("Failed to search for active punishment for " + event.name + ":")
            e.printStackTrace()
            return
        }

        try {
            val profile = Stark.instance.core.getProfileHandler().loadProfile(event.uniqueId, event.address.hostAddress)
            Stark.instance.core.getProfileHandler().profiles[profile.uuid] = profile
        } catch (e: Exception) {
            Stark.instance.logger.info("Failed to load " + event.name + "'s target:")
            e.printStackTrace()
            event.loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
            event.kickMessage = ChatColor.RED.toString() + "Failed to load your target.\nTry reconnecting later!"
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncPlayerChatEventLow(event: AsyncPlayerChatEvent) {
        val player = event.player
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)!!

        if (!event.isCancelled && profile.getActivePunishment(ProfilePunishmentType.MUTE) != null) {
            player.sendMessage("${ChatColor.RED}You're currently muted.")
            event.isCancelled = true
            return
        }

        val rank = profile.getRank()
        val prefix = rank.prefix

        Stark.instance.logger.info(rank.displayName + " - " + prefix) // compile

        event.isCancelled = true
        for (p in event.recipients) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + player.name + ChatColor.RESET + ": " + event.message)
        }//compile
    //    event.format = ChatColor.translateAlternateColorCodes('&', "$prefix%s${ChatColor.RESET}: %s")
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        event.joinMessage = null

        val player = event.player
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)

        profile!!.apply()
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        event.quitMessage = null

        val profile = Stark.instance.core.getProfileHandler().getByUUID(event.player.uniqueId)

        if (profile != null) {
            if (profile.attachment != null) {
                event.player.removeAttachment(profile.attachment)
            }
        }

        Stark.instance.core.getProfileHandler().profiles.remove(event.player.uniqueId)
    }

}