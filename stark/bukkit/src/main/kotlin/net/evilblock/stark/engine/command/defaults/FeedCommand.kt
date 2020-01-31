package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object FeedCommand {
    @Command(["feed"], "essentials.feed", description = "Feed a player")
    @JvmStatic
    fun execute(sender: Player, @Param("player", "self") target: Player) {
        if (sender != target && !sender.hasPermission("essentials.feed.other")) {
            sender.sendMessage("${ChatColor.RED}No permission to feed other players.")
            return
        }

        target.foodLevel = 20
        target.saturation = 10.0f

        if (sender != target) {
            sender.sendMessage(target.displayName + ChatColor.GOLD + " has been fed.")
        }

        target.sendMessage("${ChatColor.GOLD}You have been fed.")
    }
}