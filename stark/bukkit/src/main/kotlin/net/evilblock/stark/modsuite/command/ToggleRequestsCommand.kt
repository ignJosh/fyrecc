package net.evilblock.stark.modsuite.command

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.modsuite.options.ModOptionsHandler
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ToggleRequestsCommand {
    @Command(["tsr", "togglereports", "togglerequests"], permission = "stark.staff", description = "Toggle receiving staff requests", async = true)
    @JvmStatic
    fun execute(player: Player) {
        val options = ModOptionsHandler.get(player)
        options.receivingRequests = !options.receivingRequests

        ModOptionsHandler.update(player.uniqueId, options)

        if (options.receivingRequests) {
            player.sendMessage("${ChatColor.GREEN}You're now receiving requests and reports.")
        } else {
            player.sendMessage("${ChatColor.RED}You're no longer receiving requests and reports.")
        }
    }
}