package net.evilblock.stark.whitelist

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import net.evilblock.stark.Stark
import net.evilblock.stark.core.whitelist.WhitelistType
import com.google.gson.JsonObject
import java.util.*
import java.util.concurrent.TimeUnit

class WhitelistMessageListeners : MessageListener {

    @IncomingMessageHandler("WHITELIST_UPDATE")
    fun onWhitelistUpdate(data: JsonObject) {
        Stark.instance.core.whitelist.setMode(WhitelistType.valueOf(data.get("mode").asString), false)

        for (player in Stark.instance.proxy.players) {
            val hasWhitelist = Stark.instance.core.whitelist.getWhitelist(player.uniqueId).isAboveOrEqual(Stark.instance.core.whitelist.modeType)
            val hasPermission = player.hasPermission(Stark.instance.core.whitelist.modeType.getPermission())

            if (!hasWhitelist && !hasPermission) {
                Stark.instance.proxy.scheduler.schedule(Stark.instance, {
                    player.disconnect(Stark.instance.core.whitelist.modeType.disallowMessage)
                }, 1L, TimeUnit.MILLISECONDS)
            }
        }
    }

    @IncomingMessageHandler("VERIFIED_UPDATE")
    fun onVerifiedUpdate(data: JsonObject) {
        val status = data.get("status").asBoolean
        val playerUuid = UUID.fromString(data.get("playerUuid").asString)

        if (status) {
            Stark.instance.core.whitelist.verified.add(playerUuid)
        } else {
            Stark.instance.core.whitelist.verified.remove(playerUuid)
        }
    }

}