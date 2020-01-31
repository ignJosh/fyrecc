package net.evilblock.stark.util

import net.evilblock.stark.Stark
import com.mongodb.BasicDBList
import java.util.*

object UUIDUtils {

    @JvmStatic
    fun formatPretty(uuid: UUID): String {
        return Stark.instance.core.uuidCache.name(uuid) + " [" + uuid + "]"
    }

    @JvmStatic
    fun uuidsToStrings(toConvert: Collection<UUID>?): BasicDBList {
        if (toConvert == null || toConvert.isEmpty()) {
            return BasicDBList()
        }

        val dbList = BasicDBList()
        for (uuid in toConvert) {
            dbList.add(uuid.toString())
        }

        return dbList
    }

}