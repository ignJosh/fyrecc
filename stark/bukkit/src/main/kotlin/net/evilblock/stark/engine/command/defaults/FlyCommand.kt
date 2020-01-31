package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object FlyCommand {
    @Command(["fly"], permission = "essentials.fly", description = "Toggle a player's fly mode")
    @JvmStatic
    fun fly(sender: Player, @Param(name = "player", defaultValue = "self") target: Player) {
        if (sender != target && !sender.hasPermission("essentials.fly.other")) {
            sender.sendMessage("${ChatColor.RED}No permission to set other player's fly mode.")
            return
        }

        target.allowFlight = !target.allowFlight

        if (sender != target) {
            sender.sendMessage(target.displayName + "${ChatColor.GOLD}'s fly mode was set to ${ChatColor.WHITE}" + target.allowFlight + "${ChatColor.GOLD}.")
        }

        target.sendMessage("${ChatColor.GOLD}Your fly mode was set to ${ChatColor.WHITE}" + target.allowFlight + "${ChatColor.GOLD}.")
    }
}