package net.evilblock.stark.uuid

import net.evilblock.stark.Stark
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

class UUIDListeners : Listener {

    @EventHandler
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {
        Stark.instance.core.uuidCache.update(event.uniqueId, event.name)
    }

}