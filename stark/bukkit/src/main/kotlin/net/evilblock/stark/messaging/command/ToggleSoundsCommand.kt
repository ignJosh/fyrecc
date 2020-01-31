package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ToggleSoundsCommand {
    @Command(["sounds", "togglesounds"], description = "Toggle messaging sounds", async = true)
    @JvmStatic
    fun execute(player: Player) {
        val toggle = Stark.instance.messagingManager.toggleSounds(player.uniqueId)

        if (toggle) {
            player.sendMessage(ChatColor.YELLOW.toString() + "Messaging sounds have been disabled.")
        } else {
            player.sendMessage(ChatColor.YELLOW.toString() + "Messaging sounds have been enabled.")
        }
    }
}