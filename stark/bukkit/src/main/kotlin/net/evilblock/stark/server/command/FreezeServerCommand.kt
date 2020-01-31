package net.evilblock.stark.server.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object FreezeServerCommand {
    @Command(["freezeserver"], permission = "stark.admin.freezeserver", description = "Freeze the server. Normal players won't be able to move or interact")
    fun execute(sender: CommandSender) {
        Stark.instance.serverHandler.frozen = !Stark.instance.serverHandler.frozen

        Stark.instance.server.onlinePlayers.forEach {
            if (it.hasPermission("stark.staff")) {
                it.sendMessage("${ChatColor.RED}${ChatColor.BOLD}The server has been " + (if (Stark.instance.serverHandler.frozen) "" else "un") + "frozen by ${sender.name}.")
            } else {
                it.sendMessage("${ChatColor.RED}${ChatColor.BOLD}The server has been " + (if (Stark.instance.serverHandler.frozen) "" else "un") + "frozen.")
            }
        }

        Bukkit.getConsoleSender().sendMessage("${ChatColor.RED}${ChatColor.BOLD}The server has been " + (if (Stark.instance.serverHandler.frozen) "" else "un") + "frozen by ${sender.name}.")
    }
}