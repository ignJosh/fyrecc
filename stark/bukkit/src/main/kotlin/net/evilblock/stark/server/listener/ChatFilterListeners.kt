package net.evilblock.stark.server.listener

import net.evilblock.stark.Stark
import net.evilblock.stark.server.chat.ServerChatSettings
import com.google.common.collect.Maps
import org.bukkit.event.EventHandler
import mkremins.fanciful.FancyMessage
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.UUID

class ChatFilterListeners : Listener {

    companion object {
        val DELAY_MESSAGE: FancyMessage = FancyMessage("")
                .then("Purchase a rank at ").color(ChatColor.RED)
                .then("buy.kihar.net").link("http://buy.kihar.net")
                .tooltip("${ChatColor.GREEN}Click to visit our store").color(ChatColor.YELLOW).style(ChatColor.UNDERLINE)
                .then(" to bypass this restriction.").color(ChatColor.RED)
    }

    private val badMessages: MutableMap<UUID, String> = Maps.newConcurrentMap()

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerChatLowest(event: AsyncPlayerChatEvent) {
        val chatFilter = Stark.instance.serverHandler.bannedRegexes

        for (filter in chatFilter) {
            if (filter.pattern.matcher(event.message).find()) {
                this.badMessages[event.player.uniqueId] = event.message
                break
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    fun onPlayerChatHigh(event: AsyncPlayerChatEvent) {
        val player = event.player

        if (player.hasMetadata("NoSpamCheck")) {
            return
        }

        if (ServerChatSettings.muted && !player.hasPermission("stark.mutechat.bypass")) {
            player.sendMessage("${ChatColor.RED}The chat is currently muted.")
            event.isCancelled = true
            return
        }

        val session = ServerChatSettings.slowed
        if (session != null && !player.hasPermission("stark.slowchat.bypass")) {
            val time = session.players[player.uniqueId]
            if (time == null || time < System.currentTimeMillis()) {
                session.players[player.uniqueId] = System.currentTimeMillis() + session.duration
            } else {
                player.sendMessage("${ChatColor.RED}The chat is currently slowed. You can send another message in ${(time - System.currentTimeMillis()) / 1000} seconds.")
                event.isCancelled = true
                return
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChatMonitor(event: AsyncPlayerChatEvent) {
        val player = event.player

        if (this.badMessages.containsKey(player.uniqueId)) {
            if (event.player.hasMetadata("NoSpamCheck")) {
                return
            }

            event.recipients.clear()
            event.recipients.add(event.player)

            val toSend = FancyMessage("[Filtered] ")
                    .color(ChatColor.RED)
                    .tooltip("${ChatColor.YELLOW}This message was hidden from public chat.")
                    .then(event.format.format(player.displayName, event.message))

            for (other in Bukkit.getOnlinePlayers()) {
                if (other.hasPermission("stark.staff")) {
                    toSend.send(other)
                }
            }

            this.badMessages.remove(player.uniqueId)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onQuit(event: PlayerQuitEvent) {
        this.badMessages.remove(event.player.uniqueId)
    }

}