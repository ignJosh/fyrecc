package net.evilblock.stark.core.rank

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import net.evilblock.stark.core.StarkCore
import com.google.gson.JsonObject

object RankMessageListeners : MessageListener {

    @IncomingMessageHandler("RANK_UPDATE")
    fun onRankUpdate(data: JsonObject) {
        val id = data["id"].asString

        when (data["action"].asString) {
            "UPDATE" -> {
                StarkCore.instance.rankHandler.pullRankUpdate(id)
            }
            "DELETE" -> {
                StarkCore.instance.rankHandler.getMutableRankMap().remove(id)
            }
        }
    }

}