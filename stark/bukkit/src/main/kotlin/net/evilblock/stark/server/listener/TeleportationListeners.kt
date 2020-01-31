package net.evilblock.stark.server.listener

import org.bukkit.Location
import java.util.UUID
import java.util.HashMap
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerTeleportEvent

class TeleportationListeners : Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun onPlayerTeleport(event: PlayerTeleportEvent) {
        val player = event.player
        val cause = event.cause

        if (cause.name.contains("PEARL") || cause.name.contains("PORTAL")) {
            return
        }

        if (player.hasPermission("essentials.teleport")) {
            lastLocation[player.uniqueId] = event.from
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        if (player.hasPermission("essentials.teleport")) {
            lastLocation[player.uniqueId] = player.location
        }
    }

    companion object {
        @JvmStatic val lastLocation = HashMap<UUID, Location>()
    }
}