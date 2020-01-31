package net.evilblock.stark.availability

import net.evilblock.stark.Stark

class AvailabilityHeartbeatRunnable : Runnable {

    override fun run() {
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.proxy.players.forEach { player ->
                Stark.instance.core.availabilityHandler.update(player.uniqueId, true, Stark.instance.proxy.name, null)
            }
        }
    }

}