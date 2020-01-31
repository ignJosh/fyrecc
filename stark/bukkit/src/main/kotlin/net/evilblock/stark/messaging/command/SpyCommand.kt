package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object SpyCommand {
    @Command(["spy"], permission = "stark.message.spy", description = "Toggle global private message spying")
    @JvmStatic
    fun spy(sender: Player) {
        val contains = Stark.instance.messagingManager.globalSpy.contains(sender.uniqueId)

        if (contains) {
            Stark.instance.messagingManager.globalSpy.remove(sender.uniqueId)
        } else {
            Stark.instance.messagingManager.globalSpy.add(sender.uniqueId)
        }

        sender.sendMessage("${ChatColor.GOLD}Global chat spy has been set to ${ChatColor.WHITE}$contains${ChatColor.GOLD}.")
    }
}