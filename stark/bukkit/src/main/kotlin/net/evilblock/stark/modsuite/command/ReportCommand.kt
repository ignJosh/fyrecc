package net.evilblock.stark.modsuite.command

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.modsuite.ModRequestHandler
import net.evilblock.stark.modsuite.event.PlayerRequestReportEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.concurrent.TimeUnit

object ReportCommand {
    @Command(["report"], description = "Report a player for violating the rules", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("player") target: Player, @Param("reason", wildcard = true) reason: String) {
        if (player == target) {
            player.sendMessage("${ChatColor.RED}You can't report yourself...")
            return
        }

        if (target.hasPermission("stark.staff")) {
            player.sendMessage("${ChatColor.RED}You can't report that player.")
            return
        }

        if (ModRequestHandler.hasRequestCooldown(player)) {
            player.sendMessage("${ChatColor.RED}You can only make one staff request every 2 minutes.")
            return
        }

        val event = PlayerRequestReportEvent(player)
        Bukkit.getServer().pluginManager.callEvent(event)

        if (event.isCancelled) {
            if (event.cancelMessage != null) {
                player.sendMessage(event.cancelMessage)
            } else {
                player.sendMessage("${ChatColor.RED}Unknown cancellation.")
            }
        } else {
            ModRequestHandler.addRequestCooldown(player, 2L, TimeUnit.SECONDS)
            ModRequestHandler.incrementReportCount(target)

            val data = mapOf(
                    "serverName" to Bukkit.getServerId(),
                    "senderName" to player.name,
                    "reportedUuid" to target.uniqueId.toString(),
                    "reportedName" to target.name,
                    "reason" to reason
            )

            Stark.instance.core.globalMessageChannel.sendMessage(Message("REPORT", data))

            player.sendMessage("${ChatColor.GREEN}We have received your report.")
        }
    }

}