package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object MessageCommand {
    @Command(["message", "msg", "m", "whisper", "w", "tell", "t"], description = "Send a player a private message", async = true)
    @JvmStatic
    fun message(sender: Player, @Param(name = "player") target: Player, @Param(name = "message", wildcard = true) message: String) {
        if (sender.uniqueId == target.uniqueId) {
            sender.sendMessage("${ChatColor.RED}You can't message yourself.")
            return
        }

        if (!Stark.instance.messagingManager.canMessage(sender, target)) {
            return
        }

        Stark.instance.messagingManager.sendMessage(sender, target, message)
    }
}