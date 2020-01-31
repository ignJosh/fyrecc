package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object MoreCommand {
    @Command(["more"], permission = "essentials.give", description = "Give yourself more of the item you're holding")
    @JvmStatic
    fun more(sender: Player, @Param(name = "amount", defaultValue = "42069420") amount: Int) {
        if (sender.itemInHand == null) {
            sender.sendMessage("${ChatColor.RED}You must be holding an item.")
            return
        }

        if (amount == 42069420) {
            sender.itemInHand.amount = 64
        } else {
            sender.itemInHand.amount = Math.min(64, sender.itemInHand.amount + amount)
        }

        sender.sendMessage("${ChatColor.GOLD}There you go.")
    }
}