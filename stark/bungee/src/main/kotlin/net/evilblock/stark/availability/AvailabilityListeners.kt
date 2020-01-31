package net.evilblock.stark.availability

import net.evilblock.stark.Stark
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class AvailabilityListeners : Listener {

    @EventHandler
    fun onPostLoginEvent(event: PostLoginEvent) {
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.core.availabilityHandler.update(event.player.uniqueId, true, Stark.instance.proxy.name, null)
        }
    }

    @EventHandler
    fun onPlayerDisconnectEvent(event: PlayerDisconnectEvent) {
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.core.availabilityHandler.update(event.player.uniqueId, false, Stark.instance.proxy.name, null)
        }
    }

}