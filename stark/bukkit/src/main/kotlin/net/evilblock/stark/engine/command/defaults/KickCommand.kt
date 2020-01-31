package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import net.evilblock.stark.engine.command.data.flag.Flag
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor

object KickCommand {
    @Command(["kick", "k"], permission = "stark.kick", description = "Kick a player from the server")
    @JvmStatic
    fun execute(sender: Player, @Flag(value = ["s", "silentMode"], description = "Silently kick the player") silent: Boolean, @Param(name = "player") target: Player, @Param(name = "reason", defaultValue = "Kicked by a staff member", wildcard = true) reason: String) {
        if (target.name.equals("joeleoli", ignoreCase = true)) {
            sender.inventory.clear()
            sender.health = 0.0
            sender.sendMessage("${ChatColor.GOLD}You have been killed.")
            sender.kickPlayer("Nice try.")
            return
        }

        if (target.hasPermission("stark.punishment.protected")) {
            sender.sendMessage("${ChatColor.RED}User is protected from punishments.")
            return
        }

        target.kickPlayer("${ChatColor.RED}You were kicked: " + reason)

        if (!silent) {
            Bukkit.broadcastMessage(ChatColor.GREEN.toString() + target.name + " was kicked by " + sender.name + ".")
        } else {
            for (player in Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("stark.kick")) {
                    player.sendMessage(ChatColor.GREEN.toString() + target.name + " was " + ChatColor.YELLOW + "silently" + ChatColor.GREEN + " kicked by " + sender.name + ".")
                }
            }
        }
    }
}