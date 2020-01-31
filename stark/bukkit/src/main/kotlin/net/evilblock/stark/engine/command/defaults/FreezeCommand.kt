package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object FreezeCommand {
    @Command(["freeze"], permission = "essentials.freeze", description = "Freeze a player. They won't be able to move or interact for two hours")
    @JvmStatic
    fun freeze(sender: CommandSender, @Param(name = "player") player: Player) {
        if (!sender.isOp && player.hasPermission("stark.staff")) {
            sender.sendMessage("${ChatColor.RED}You can't freeze that player.")
            return
        }

        Stark.instance.serverHandler.freeze(player)
        sender.sendMessage("${player.displayName}${ChatColor.GOLD} has been frozen.")
    }

    @Command(["unfreeze"], permission = "essentials.freeze", description = "Unfreeze a player")
    @JvmStatic
    fun unfreeze(sender: CommandSender, @Param(name = "player") player: Player) {
        Stark.instance.serverHandler.unfreeze(player.uniqueId)
        sender.sendMessage("${player.displayName}${ChatColor.GOLD} has been unfrozen.")
    }
}