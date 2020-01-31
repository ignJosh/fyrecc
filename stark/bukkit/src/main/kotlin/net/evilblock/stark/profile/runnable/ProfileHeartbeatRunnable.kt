package net.evilblock.stark.profile.runnable

import net.evilblock.stark.Stark
import net.evilblock.stark.core.StarkCore

class ProfileHeartbeatRunnable : Runnable {

    override fun run() {
        // Heartbeat profiles every 30 seconds
        for (player in Stark.instance.server.onlinePlayers) {
            val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(player.uniqueId)
            Stark.instance.core.getProfileHandler().profiles[player.uniqueId] = profile
        }
    }

}