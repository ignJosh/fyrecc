package net.evilblock.stark.whitelist

import net.evilblock.stark.Stark
import net.evilblock.stark.core.whitelist.WhitelistType
import net.evilblock.stark.event.LoginProfileLoadedEvent
import net.evilblock.stark.util.ChatUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.ServerPing
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class WhitelistListeners : Listener {

    @EventHandler
    fun onProxyPingEvent(event: ProxyPingEvent) {
        val serverPing = event.response

        if (Stark.instance.core.whitelist.modeType == WhitelistType.MAINTENANCE) {
            serverPing.version = ServerPing.Protocol("Whitelisted", 1337)
        }
    }

    @EventHandler
    fun onLoginProfileLoadedEvent(event: LoginProfileLoadedEvent) {
        val modeType = Stark.instance.core.whitelist.modeType

        val hasWhitelist = Stark.instance.core.whitelist.getWhitelist(event.profile.uuid).isAboveOrEqual(modeType)
        val hasPermission = event.profile.getCompoundedPermissions().contains(modeType.getPermission())

        if (!hasWhitelist && !hasPermission) {
            event.isCancelled = true
            event.cancelReasons = arrayOf(ChatUtil.compile(TextComponent.fromLegacyText(ChatColor.GOLD.toString() + modeType.disallowMessage)))
        }
    }

    @EventHandler
    fun onPostLoginEvent(event: PostLoginEvent) {
        if (Stark.instance.core.whitelist.modeType == WhitelistType.MAINTENANCE) {
            event.player.sendMessage("${ChatColor.GOLD}The network is in maintenance mode, but you were able to join because of your rank.")
        }
    }

}