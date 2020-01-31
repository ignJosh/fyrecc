package net.evilblock.stark.availability

import net.evilblock.stark.Stark
import org.bukkit.Bukkit

class AvailabilityHeartbeatRunnable : Runnable {

    override fun run() {
        Stark.instance.server.onlinePlayers.forEach { player ->
            Stark.instance.core.availabilityHandler.update(player.uniqueId, true, null, Bukkit.getServerId())
        }
    }

}