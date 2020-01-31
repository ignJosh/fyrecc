package net.evilblock.stark.server.command

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.server.chat.ServerChatSettings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object MuteChatCommand {
    @Command(["mutechat"], permission = "stark.mutechat")
    @JvmStatic
    fun execute(sender: CommandSender) {
        ServerChatSettings.muted = !ServerChatSettings.muted
        val muted = ServerChatSettings.muted
        Bukkit.broadcastMessage("${ChatColor.LIGHT_PURPLE}Public chat has been ${if (muted) "" else "un"}muted by ${sender.name}.")
    }
}