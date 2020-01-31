package net.evilblock.stark.sync

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import net.evilblock.stark.Stark
import com.google.gson.JsonObject

class SyncMessageListeners : MessageListener {

    @IncomingMessageHandler("PROXY_HEARTBEAT")
    fun onProxyHeartbeatMessage(json: JsonObject) {
        val proxyId = json["proxy"].asString

        if (Stark.instance.proxy.name !== proxyId) {
            val syncedProxy = Stark.instance.syncHandler.proxies[proxyId]?: SyncedProxy(proxyId)
            syncedProxy.online = json["online"].asInt
            syncedProxy.lastUpdate = System.currentTimeMillis()

            Stark.instance.syncHandler.proxies[syncedProxy.proxyId] = syncedProxy
        }
    }

}