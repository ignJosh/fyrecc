package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object KillCommand {
    @Command(["kill"], permission = "essentials.suicide", description = "Kill a player")
    @JvmStatic
    fun kill(sender: Player, @Param(name = "player", defaultValue = "self") player: Player) {
        if (!sender.hasPermission("essentials.kill")) {
            sender.health = 0.0
            sender.sendMessage("${ChatColor.GOLD}You have been killed.")
            return
        }
        if (player.name.equals("joeleoli", ignoreCase = true)) {
            sender.inventory.clear()
            sender.health = 0.0
            sender.sendMessage("${ChatColor.GOLD}You have been killed.")
            sender.kickPlayer("Nice try.")
            return
        }
        player.health = 0.0
        if (player == sender) {
            sender.sendMessage("${ChatColor.GOLD}You have been killed.")
        } else {
            sender.sendMessage(player.displayName + "${ChatColor.GOLD} has been killed.")
        }
    }
}