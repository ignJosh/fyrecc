package net.evilblock.stark.profile.punishment.command.remove

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.flag.Flag
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object UnblacklistCommand {
    @Command(["unblacklist"], permission = "stark.punishment.BLACKLIST.delete", description = "Unblacklist a player", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Flag(value = ["s", "silentMode"], description = "Silently unblacklist the player") silent: Boolean, @Param("target") target: BukkitProfile, @Param("reason", wildcard = true) reason: String) {
        var issuer: UUID? = null
        if (sender is Player) {
            issuer = sender.uniqueId
        }

        val activePunishment = target.getActivePunishment(ProfilePunishmentType.BLACKLIST)
        if (activePunishment == null) {
            sender.sendMessage("${target.getPlayerListName()} ${ChatColor.RED}is not blacklisted.")
            return
        }

        activePunishment.removedBy = issuer
        activePunishment.removedAt = System.currentTimeMillis()
        activePunishment.removalReason = reason

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("PUNISHMENT_UPDATE", mapOf("uuid" to target.uuid.toString(), "punishment" to activePunishment.uuid.toString(), "silent" to silent)))
    }
}