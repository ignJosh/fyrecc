package net.evilblock.stark.modsuite.command

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.modsuite.options.ModOptionsHandler
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ToggleStaffChatCommand {
    @Command(["tsc", "togglestaffchat"], permission = "stark.staff", description = "Toggle receiving staff chat", async = true)
    @JvmStatic
    fun toggleStaffChat(player: Player) {
        val options = ModOptionsHandler.get(player)
        options.receivingStaffChat = !options.receivingStaffChat

        ModOptionsHandler.update(player.uniqueId, options)

        if (options.receivingStaffChat) {
            player.sendMessage("${ChatColor.GREEN}You're now receiving staff chat.")
        } else {
            player.sendMessage("${ChatColor.RED}You're no longer receiving staff chat.")
        }
    }
}