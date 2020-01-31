package net.evilblock.stark.engine.protocol

import net.evilblock.stark.engine.protocol.event.ServerLaggedOutEvent
import net.evilblock.stark.util.PlayerUtils
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class LagCheck : BukkitRunnable() {

    override fun run() {
        val players = Bukkit.getOnlinePlayers()
        if (players.size >= 100) {
            var playersLagging = 0
            for (player in players) {
                if (PlayerUtils.isLagging(player)) {
                    ++playersLagging
                }
            }

            val percentage = playersLagging * 100 / players.size
            if (Math.abs(percentage) >= 30.0) {
                Bukkit.getPluginManager().callEvent(ServerLaggedOutEvent(PingAdapter.averagePing()))
            }
        }
    }

}