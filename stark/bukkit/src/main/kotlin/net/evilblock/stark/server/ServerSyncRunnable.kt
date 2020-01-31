package net.evilblock.stark.server

import net.evilblock.stark.Stark
import net.evilblock.stark.util.Reflections
import org.bukkit.Bukkit

class ServerSyncRunnable : Runnable {

    override fun run() {
        val optionalServer = Stark.instance.core.servers.getServerByName(Bukkit.getServerName())

        val server = if (optionalServer.isPresent) {
            optionalServer.get()
        } else {
            Stark.instance.core.servers.loadOrCreateServer(Bukkit.getServerName(), Bukkit.getPort())
        }

        server.lastHeartbeat = System.currentTimeMillis()
        server.currentUptime = System.currentTimeMillis() - Stark.instance.enabledAt
        server.currentTps = Reflections.getTPS()
        server.playerCount = Bukkit.getOnlinePlayers().size
        server.maxSlots = Bukkit.getMaxPlayers()
        server.whitelisted = Bukkit.hasWhitelist()

        Stark.instance.core.servers.saveServer(server)
    }

}