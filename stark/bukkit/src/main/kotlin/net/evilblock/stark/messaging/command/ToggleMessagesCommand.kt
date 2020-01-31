package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ToggleMessagesCommand {
    @Command(["togglepm", "tpm"], description = "Toggle private messaging", async = true)
    @JvmStatic
    fun execute(player: Player) {
        val toggle = Stark.instance.messagingManager.toggleMessages(player.uniqueId)

        if (toggle) {
            player.sendMessage("${ChatColor.RED}Private messages have been disabled.")
        } else {
            player.sendMessage("${ChatColor.GREEN}Private messages have been enabled.")
        }
    }
}