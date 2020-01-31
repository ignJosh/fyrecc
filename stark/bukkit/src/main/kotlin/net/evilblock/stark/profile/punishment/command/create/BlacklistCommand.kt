package net.evilblock.stark.profile.punishment.command.create

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.flag.Flag
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.profile.punishment.ProfilePunishment
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object BlacklistCommand {

    @Command(["blacklist"], permission = "stark.punishment.BLACKLIST.create", description = "Blacklist a player", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Flag(value = ["s", "silentMode"], description = "Silently ban the player") silent: Boolean, @Param("target") target: BukkitProfile, @Param("reason", wildcard = true) reason: String) {
        var issuer: UUID? = null
        if (sender is Player) {
            val senderProfile = Stark.instance.core.getProfileHandler().getByUUID(sender.uniqueId)
            if (senderProfile == null) {
                sender.sendMessage("${ChatColor.RED}Error verifying your target. Try again later.")
                return
            }

            if (target.getRank().displayOrder <= senderProfile.getRank().displayOrder) {
                sender.sendMessage("${ChatColor.RED}You can't blacklist this player.")
                return
            }

            issuer = sender.uniqueId
        }

        if (target.getActivePunishment(ProfilePunishmentType.BLACKLIST) != null) {
            sender.sendMessage("${target.getPlayerListName()} ${ChatColor.RED}is already blacklisted.")
            return
        }

        val punishment = ProfilePunishment()
        punishment.type = ProfilePunishmentType.BLACKLIST
        punishment.reason = reason
        punishment.issuedBy = issuer
        punishment.issuedAt = System.currentTimeMillis()

        target.punishments.add(punishment)

        Stark.instance.core.getProfileHandler().saveProfile(target)
        Stark.instance.core.globalMessageChannel.sendMessage(Message("PUNISHMENT_UPDATE", mapOf("uuid" to target.uuid.toString(), "punishment" to punishment.uuid.toString(), "silent" to silent)))
    }

}