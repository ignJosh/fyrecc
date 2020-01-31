package net.evilblock.stark.profile

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import com.google.gson.JsonObject
import net.evilblock.stark.Stark
import net.evilblock.stark.profile.grant.event.GrantRemovedEvent
import net.evilblock.stark.core.profile.punishment.ProfilePunishment
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import net.evilblock.stark.core.util.TimeUtils
import mkremins.fanciful.FancyMessage
import net.evilblock.stark.profile.grant.event.GrantCreatedEvent
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*
import java.util.concurrent.TimeUnit

class ProfileMessageListeners : MessageListener {

    @IncomingMessageHandler("GRANT_UPDATE")
    fun onGrantUpdateMessage(json: JsonObject) {
        val uuid = UUID.fromString(json.get("uuid").asString)
        val grantId = UUID.fromString(json.get("grant").asString)
        val player = Bukkit.getPlayer(uuid)

        if (player != null) {
            val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)

            for (grant in profile.rankGrants) {
                if (grant.uuid == grantId) {
                    if (grant.removedAt != null) {
                        Stark.instance.server.pluginManager.callEvent(GrantRemovedEvent(player, grant))
                    } else {
                        Stark.instance.server.pluginManager.callEvent(GrantCreatedEvent(player, grant))
                    }
                }
            }
        }
    }

    @IncomingMessageHandler("PUNISHMENT_UPDATE")
    fun onPunishmentUpdateMessage(json: JsonObject) {
        val uuid = UUID.fromString(json.get("uuid").asString)
        val punishmentId = UUID.fromString(json.get("punishment").asString)
        val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)

        for (punishment in profile.punishments) {
            if (punishment.uuid == punishmentId) {
                executePunishment(profile, punishment, json.get("silent").asBoolean)
            }
        }
    }

    private fun executePunishment(recipient: BukkitProfile, punishment: ProfilePunishment, silent: Boolean) {
        val recipientName = recipient.getPlayerListName()

        val issuerName: String = if (punishment.removedAt != null) {
            if (punishment.removedBy == null) {
                ChatColor.DARK_RED.toString() + "Console"
            } else {
                Stark.instance.core.getProfileHandler().loadProfile(punishment.removedBy!!).getPlayerListName()
            }
        } else {
            if (punishment.issuedBy == null) {
                ChatColor.DARK_RED.toString() + "Console"
            } else {
                Stark.instance.core.getProfileHandler().loadProfile(punishment.issuedBy!!).getPlayerListName()
            }
        }

        val silently = if (silent) ChatColor.YELLOW.toString() + "silently " else ""
        val player = recipient.getPlayer()

        if (punishment.removalReason == null) {
            val context = if (punishment.type == ProfilePunishmentType.BLACKLIST) "" else if (punishment.expiresAt == null) "permanently " else "temporarily "

            val staffMessage = FancyMessage("$recipientName ${ChatColor.GREEN}was $silently${ChatColor.GREEN}$context${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                    .formattedTooltip(Arrays.asList(
                            FancyMessage("Issued by: ")
                                    .color(ChatColor.YELLOW)
                                    .then(issuerName),
                            FancyMessage("Reason: ")
                                    .color(ChatColor.YELLOW)
                                    .then(punishment.reason)
                                    .color(ChatColor.RED),
                            FancyMessage("Duration: ")
                                    .color(ChatColor.YELLOW)
                                    .then(if (punishment.expiresAt == null) "Permanent" else TimeUtils.formatIntoDetailedString(TimeUnit.MILLISECONDS.toSeconds(punishment.expiresAt!! - System.currentTimeMillis()).toInt()))
                                    .color(ChatColor.RED)
                    ))

            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("stark.punishment." + punishment.type.name + ".view")) {
                    staffMessage.send(onlinePlayer)
                } else if (!silent) {
                    onlinePlayer.sendMessage("$recipientName ${ChatColor.GREEN}was ${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                }
            }

            // kick players
            if (punishment.type !== ProfilePunishmentType.MUTE) {
                // kick player if they're online
                if (player != null) {
                    Stark.instance.server.scheduler.runTask(Stark.instance) {
                        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', StringUtils.join(punishment.type.kickMessages, "\n")))
                    }
                }

                // kick players sharing ip addresses
                for (it in Stark.instance.server.onlinePlayers) {
                    if (it.uniqueId != recipient.uuid) {
                        if (recipient.ipAddresses.contains(it.address.address.hostAddress)) {
                            Stark.instance.server.scheduler.runTask(Stark.instance) {
                                it.kickPlayer(ChatColor.translateAlternateColorCodes('&', StringUtils.join(punishment.type.kickMessages, "\n")))
                            }
                        }
                    }
                }
            }
        } else {
            val staffMessage = FancyMessage("$recipientName ${ChatColor.GREEN}was $silently${ChatColor.GREEN}un${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                    .formattedTooltip(Arrays.asList(
                            FancyMessage("Removed by: ")
                                    .color(ChatColor.YELLOW)
                                    .then(issuerName),
                            FancyMessage("Reason: ")
                                    .color(ChatColor.YELLOW)
                                    .then(punishment.removalReason)
                                    .color(ChatColor.RED)
                    ))

            for (onlinePlayer in Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("stark.punishment." + punishment.type.name + ".view")) {
                    staffMessage.send(onlinePlayer)
                } else if (!silent) {
                    onlinePlayer.sendMessage("$recipientName ${ChatColor.GREEN}was un${punishment.type.action} by $issuerName${ChatColor.GREEN}.")
                }
            }
        }
    }

}