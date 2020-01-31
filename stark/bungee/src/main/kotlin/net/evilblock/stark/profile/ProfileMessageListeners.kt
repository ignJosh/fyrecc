package net.evilblock.stark.profile

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import net.evilblock.stark.Stark
import com.google.gson.JsonObject
import java.util.*

class ProfileMessageListeners : MessageListener {

    @IncomingMessageHandler("GRANT_UPDATE")
    fun onGrantUpdate(data: JsonObject) {
        val uuid = UUID.fromString(data.get("uuid").asString)
        val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)

        profile.apply()
    }

    @IncomingMessageHandler("PUNISHMENT_UPDATE")
    fun onPunishmentUpdate(data: JsonObject) {
        val uuid = UUID.fromString(data.get("uuid").asString)
        Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)
    }

}