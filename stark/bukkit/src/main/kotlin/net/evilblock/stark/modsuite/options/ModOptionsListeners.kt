package net.evilblock.stark.modsuite.options

import net.evilblock.stark.Stark
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class ModOptionsListeners : Listener {

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
            ModOptionsHandler.cache[event.player.uniqueId] = ModOptionsHandler.fetch(event.player.uniqueId)
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        ModOptionsHandler.cache.remove(event.player.uniqueId)
    }

}