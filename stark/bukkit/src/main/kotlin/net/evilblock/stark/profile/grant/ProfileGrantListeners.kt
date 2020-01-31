package net.evilblock.stark.profile.grant

import net.evilblock.stark.Stark
import net.evilblock.stark.profile.grant.event.GrantCreatedEvent
import net.evilblock.stark.profile.grant.event.GrantRemovedEvent
import net.evilblock.stark.core.util.TimeUtils
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ProfileGrantListeners : Listener {

    @EventHandler
    fun onGrantCreatedEvent(event: GrantCreatedEvent) {
        event.player.sendMessage("${ChatColor.GREEN}You've been granted the ${ChatColor.translateAlternateColorCodes('&', event.grant.rank.gameColor + event.grant.rank.displayName)} ${ChatColor.GREEN}rank for a period of ${ChatColor.YELLOW}${if (event.grant.expiresAt == null) "forever" else TimeUtils.formatIntoDetailedString(((event.grant.expiresAt!! - System.currentTimeMillis()) / 1000).toInt())}${ChatColor.GREEN}.")
        Stark.instance.core.getProfileHandler().getByUUID(event.player.uniqueId)?.apply()
    }

    @EventHandler
    fun onGrantRemovedEvent(event: GrantRemovedEvent) {
        Stark.instance.core.getProfileHandler().getByUUID(event.player.uniqueId)?.apply()
    }

}