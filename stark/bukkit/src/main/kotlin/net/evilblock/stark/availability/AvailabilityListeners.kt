package net.evilblock.stark.availability

import net.evilblock.stark.Stark
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

class AvailabilityListeners : Listener {

    @EventHandler
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        Stark.instance.core.availabilityHandler.update(event.uniqueId, true, null, Bukkit.getServerId())
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        Stark.instance.core.availabilityHandler.update(event.player.uniqueId, false, null, Bukkit.getServerId())
    }

}