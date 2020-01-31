package net.evilblock.stark.core.rank

import net.evilblock.stark.core.StarkCore
import org.bson.Document
import java.util.ArrayList

object RankSerializer {

    @JvmStatic
    fun deserialize(document: Document): Rank {
        val rank = Rank(document.getString("id"))

        rank.permissions = ArrayList()
        rank.displayName = document.getString("displayName")
        rank.displayOrder = document.getInteger("displayOrder")!!
        rank.prefix = document.getString("prefix")
        rank.playerListPrefix = document.getString("playerListPrefix")
        rank.gameColor = document.getString("gameColor")
        rank.default = document.getBoolean("default")!!
        rank.inherits = document.getList("inherits", String::class.java)

        if (document.containsKey("hidden")) {
            rank.hidden = document.getBoolean("hidden")
        }

        if (document.containsKey("permissions")) {
            rank.permissions.addAll(document.getList("permissions", String::class.java))
        }

        return rank
    }

    @JvmStatic
    fun serialize(rank: Rank): Document {
        val document = Document("id", rank.id)

        document["displayName"] = rank.displayName
        document["displayOrder"] = rank.displayOrder
        document["prefix"] = rank.prefix.replace(StarkCore.COLOR_CODE_CHAR, '&')
        document["playerListPrefix"] = rank.playerListPrefix.replace(StarkCore.COLOR_CODE_CHAR, '&')
        document["gameColor"] = rank.gameColor.replace(StarkCore.COLOR_CODE_CHAR, '&')
        document["inherits"] = rank.inherits
        document["hidden"] = rank.hidden
        document["default"] = rank.default
        document["permissions"] = rank.permissions

        return document
    }

}