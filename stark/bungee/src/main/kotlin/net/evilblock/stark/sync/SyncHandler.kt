package net.evilblock.stark.sync

import nig.cock.nigger.message.Message
import net.evilblock.stark.Stark
import net.evilblock.stark.core.server.Server
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

class SyncHandler {

    val proxies: MutableMap<String, SyncedProxy> = hashMapOf()

    fun getGlobalOnlineCount(): Int {
        return proxies.values.sumBy { proxy ->
            return if (proxy.isOnline()) {
                proxy.online
            } else {
                0
            }
        } + Stark.instance.proxy.onlineCount
    }

    fun broadcastGlobalOnlineCount() {
        Stark.instance.core.globalMessageChannel.sendMessage(Message("GLOBAL_COUNT", mapOf("globalCount" to getGlobalOnlineCount().toString())))
    }

    fun heartbeatProxy() {
        val data = mapOf(
                "proxy" to Stark.instance.proxy.name,
                "online" to Stark.instance.proxy.onlineCount
        )

        Stark.instance.proxyMessageChannel.sendMessage(Message("PROXY_HEARTBEAT", data))
    }

    fun findNextBestHubServer(proxiedPlayer: ProxiedPlayer): ServerInfo? {
        val hubServers = arrayListOf<Server>()

        for (server in Stark.instance.proxy.servers.values) {
            if (Stark.instance.isRestrictedHubServer(server)) {
                if (proxiedPlayer.hasPermission("stark.bungee.restricted-hub")) {
                    val optionalServer = Stark.instance.core.servers.getServerByName("Restricted-Hub")

                    if (optionalServer.isPresent) {
                        val restrictedHub = optionalServer.get()

                        if (restrictedHub.isOnline() && restrictedHub.playerCount < restrictedHub.maxSlots) {
                            return Stark.instance.proxy.getServerInfo("Restricted-Hub")
                        }
                    }
                }
            } else if (Stark.instance.isHubServer(server)) {
                val optionalServer = Stark.instance.core.servers.getServerByName(server.name)

                if (optionalServer.isPresent) {
                    val hub = optionalServer.get()

                    if (hub.isOnline() && hub.playerCount < hub.maxSlots) {
                        hubServers.add(hub)
                    }
                }
            }
        }

        if (hubServers.isEmpty()) {
            return null
        }

        return Stark.instance.proxy.getServerInfo(hubServers.sortedBy { server -> server.playerCount }.reversed().first().serverName)
    }

}