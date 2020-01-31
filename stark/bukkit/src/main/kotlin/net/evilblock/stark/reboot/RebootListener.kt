package net.evilblock.stark.reboot

import net.evilblock.stark.Stark
import net.evilblock.stark.util.event.HourEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import java.util.concurrent.TimeUnit

class RebootListener : Listener {

    @EventHandler
    fun onHour(event: HourEvent) {
        if (Stark.instance.rebootHandler.rebootTimes.contains(event.hour)) {
            Stark.instance.rebootHandler.rebootServer(5, TimeUnit.MINUTES)
        }
    }

}