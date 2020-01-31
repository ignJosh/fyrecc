package net.evilblock.stark.modsuite.command

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.modsuite.options.ModOptionsHandler
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object StaffChatCommand {
    @Command(["staffchat", "sc"], permission = "stark.staff", description = "Send a message through staff chat.", async = true)
    @JvmStatic
    fun execute(player: Player, @Param(name = "message", wildcard = true) message: String) {
        val options = ModOptionsHandler.get(player)

        if (!options.receivingStaffChat) {
            player.sendMessage("${ChatColor.RED}You have staff chat disabled.")
            return
        }

        val data = mapOf(
                "serverName" to Bukkit.getServerId(),
                "senderName" to player.name,
                "message" to message
        )

        Stark.instance.core.globalMessageChannel.sendMessage(Message("STAFF_CHAT", data))
    }
}