package net.evilblock.stark.sync

import net.evilblock.stark.Stark
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.*
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class SyncListeners : Listener {

    @EventHandler
    fun onProxyPingEvent(event: ProxyPingEvent) {
        val serverPing = event.response
        serverPing.players = ServerPing.Players(1000, Stark.instance.syncHandler.getGlobalOnlineCount(), arrayOf())
    }

    @EventHandler
    fun onServerConnectEvent(event: ServerConnectEvent) {
        val player = event.player
        val server = player.server
        val target = event.target

        if ((player.server == null || !Stark.instance.isHubServer(server.info)) && Stark.instance.isHubServer(target)) {
            val hub = Stark.instance.syncHandler.findNextBestHubServer(player)

            if (hub != null) {
                player.sendMessage("${ChatColor.GOLD}Sending you to ${hub.name}...")
                event.target = hub
            }
        }
    }

    @EventHandler
    fun onServerKickEvent(event: ServerKickEvent) {
        val player = event.player
        val reason = TextComponent.toPlainText(*event.kickReasonComponent)
        val from = event.kickedFrom

        if (player.server == null) {
            return
        }

        if (from == player.server.info) {
            for (regex in kickRegexes) {
                if (reason.matches(regex.toRegex())) {
                    val hub = Stark.instance.syncHandler.findNextBestHubServer(player)

                    if (hub != null) {
                        player.sendMessage("${ChatColor.GOLD}Sending you to ${hub.name}...")
                        event.isCancelled = true
                        event.cancelServer = hub
                    }
                }
            }
        }
    }

    companion object {
        private val kickRegexes = arrayListOf("You have been kicked", "Server is restarting", "Server closed", "Internal Exception")
    }

}