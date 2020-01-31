package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object IgnoreListClearCommand {
    @Command(["ignore clear"], description = "Clear your ignore list", async = true)
    @JvmStatic
    fun execute(player: Player) {
        Stark.instance.messagingManager.clearIgnoreList(player.uniqueId)
        player.sendMessage("${ChatColor.YELLOW}You've cleared your ignore list.")
    }
}