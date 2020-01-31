package net.evilblock.stark.server.command

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.server.chat.ServerChatSettings
import net.evilblock.stark.server.chat.SlowedChatSession
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object SlowChatCommand {
    @Command(["slowchat"], permission = "stark.slowchat")
    @JvmStatic
    fun execute(sender: CommandSender, @Param("duration", defaultValue = "0") duration: Int) {
        if (duration > 30) {
            sender.sendMessage("${ChatColor.RED}You can't slow the chat for this long.")
            return
        }

        if (duration == 0) {
            val session = ServerChatSettings.slowed
            if (session == null) {
                sender.sendMessage("${ChatColor.RED}The chat isn't being slowed.")
                return
            } else {
                ServerChatSettings.slowed = null
                Bukkit.broadcastMessage("${ChatColor.LIGHT_PURPLE}Public chat has been unslowed by ${sender.name}.")
            }
            return
        }

        val session = SlowedChatSession(sender.name, duration * 1000L)
        ServerChatSettings.slowed = session
        Bukkit.broadcastMessage("${ChatColor.LIGHT_PURPLE}Public chat has been slowed by ${sender.name}.")
    }
}