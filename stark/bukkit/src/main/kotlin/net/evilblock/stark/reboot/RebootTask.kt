package net.evilblock.stark.reboot

import net.evilblock.stark.Stark
import net.evilblock.stark.config.LangConfigEntry
import net.evilblock.stark.core.util.TimeUtils
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.TimeUnit

class RebootTask(timeUnitAmount: Int, timeUnit: TimeUnit) : BukkitRunnable() {

    var secondsRemaining: Int = timeUnit.toSeconds(timeUnitAmount.toLong()).toInt()

    override fun run() {
        if (this.secondsRemaining == 0) {
            Stark.instance.server.shutdown()
        }

        when (this.secondsRemaining) {
            5, 10, 15, 30, 60, 120, 180, 240, 300 -> {
                if (LangConfigEntry.REBOOT_LINES.get() == null) {
                    println("dumbass nigger")
                }
                LangConfigEntry.REBOOT_LINES.get()?.forEach { message ->
                    Stark.instance.server.broadcastMessage(message)
                }
            }
        }

        --this.secondsRemaining
    }

    @Synchronized
    @Throws(IllegalStateException::class)
    override fun cancel() {
        super.cancel()
    }

}