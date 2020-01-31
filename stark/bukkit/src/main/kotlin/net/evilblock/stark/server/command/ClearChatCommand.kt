package net.evilblock.stark.server.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable

object ClearChatCommand {
    @Command(["clearchat", "cc"], permission = "stark.clearchat", async = true)
    @JvmStatic
    fun execute(sender: CommandSender) {
        object: BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    if (!it.hasPermission("stark.staff")) {
                        for (i in 1..100) it.sendMessage("")
                    }

                    if (it.hasPermission("stark.staff")) {
                        it.sendMessage("${ChatColor.LIGHT_PURPLE}The chat has been cleared by " + sender.name + ".")
                    } else {
                        it.sendMessage("${ChatColor.LIGHT_PURPLE}The chat has been cleared by a staff member.")
                    }
                }
            }
        }.runTaskAsynchronously(Stark.instance)

        Bukkit.getConsoleSender().sendMessage("${ChatColor.LIGHT_PURPLE}The chat has been cleared by " + sender.name + ".")
    }
}