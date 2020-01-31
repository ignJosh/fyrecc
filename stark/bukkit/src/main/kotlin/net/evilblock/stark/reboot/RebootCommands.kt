package net.evilblock.stark.reboot

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.core.util.TimeUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

object RebootCommands {
    @Command(["reboot"], permission = "stark.reboot")
    @JvmStatic
    fun execute(sender: CommandSender, @Param(name = "time") unparsedTime: String) {
        try {
            val time = TimeUtils.parseTime(unparsedTime.toLowerCase())
            Stark.instance.rebootHandler.rebootServer(time, TimeUnit.SECONDS)
            sender.sendMessage("${ChatColor.GREEN}Started auto reboot.")
        } catch (ex: Exception) {
            sender.sendMessage(ChatColor.RED.toString() + ex.message)
        }

    }

    @Command(["reboot cancel"], permission = "stark.reboot")
    @JvmStatic
    fun execute(sender: CommandSender) {
        if (!Stark.instance.rebootHandler.isRebooting()) {
            sender.sendMessage("${ChatColor.RED}No reboot has been scheduled.")
        } else {
            Stark.instance.rebootHandler.rebootTask?.cancel()

            Stark.instance.server.broadcastMessage("")
            Stark.instance.server.broadcastMessage("${ChatColor.RED}${ChatColor.BOLD}⚠ ${ChatColor.YELLOW}Server reboot cancelled!")
            Stark.instance.server.broadcastMessage("")
        }
    }
}