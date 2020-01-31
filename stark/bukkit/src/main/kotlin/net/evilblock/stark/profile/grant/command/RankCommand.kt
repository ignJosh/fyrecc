package net.evilblock.stark.profile.grant.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.core.util.TimeUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object RankCommand {
    @Command(["rank"], description = "Check your current rank and its expiration")
    @JvmStatic
    fun execute(player: Player) {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)
        if (profile != null) {
            val rank = profile.getRank()
            val grant = profile.rankGrants.filter { it.rank == rank }.toList()
            player.sendMessage("${ChatColor.GOLD}You're the ${ChatColor.translateAlternateColorCodes('&', rank.gameColor)}${rank.displayName} ${ChatColor.GOLD}rank.${if (grant.isEmpty() || grant[0].expiresAt == null) "" else "${ChatColor.GRAY}(Expires in ${TimeUtils.formatIntoDetailedString(((grant[0].expiresAt!! - System.currentTimeMillis()) / 1000).toInt())})"}")
        }
    }
}