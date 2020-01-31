package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object BroadcastCommand {
    @Command(["bc", "broadcast"], "essentials.broadcast")
    @JvmStatic
    fun execute(sender: CommandSender, @Param("message", wildcard = true) message: String) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message))
    }
}