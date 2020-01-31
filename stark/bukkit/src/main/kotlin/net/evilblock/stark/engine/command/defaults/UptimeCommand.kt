package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.core.util.TimeUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

object UptimeCommand {
    private fun uptimeColor(secs: Int): String {
        if (secs <= TimeUnit.HOURS.toSeconds(16L)) {
            return "§a"
        }
        return if (secs <= TimeUnit.HOURS.toSeconds(24L)) {
            "§e"
        } else "§c"
    }

    @Command(["uptime"], description = "Check how long the server has been up for")
    @JvmStatic
    fun uptime(sender: CommandSender) {
        val seconds = ((System.currentTimeMillis() - Stark.instance.enabledAt) / 1000).toInt()
        sender.sendMessage("${ChatColor.GOLD}The server has been running for " + uptimeColor(seconds) + TimeUtils.formatIntoDetailedString(seconds) + ChatColor.GOLD + ".")
    }
}