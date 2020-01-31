package net.evilblock.stark.profile

import net.evilblock.stark.Stark
import java.util.*

class ProfileCheckRunnable : Runnable {

    override fun run() {
        val toRemove = arrayListOf<UUID>()

        Stark.instance.core.getProfileHandler().profiles.keys.forEach {
            if (Stark.instance.proxy.getPlayer(it) == null) {
                toRemove.add(it)
            }
        }

        toRemove.forEach {
            Stark.instance.core.getProfileHandler().profiles.remove(it)
        }

        if (toRemove.isNotEmpty()) {
            Stark.instance.logger.info("Removed " + toRemove.size + " cached profiles")
        }
    }

}