package net.evilblock.stark.modsuite.options

import net.evilblock.stark.Stark
import org.bukkit.entity.Player
import redis.clients.jedis.Jedis
import java.util.*
import kotlin.collections.HashMap

object ModOptionsHandler {

    val cache = HashMap<UUID, ModOptions>()

    fun get(player: Player): ModOptions {
        return cache[player.uniqueId]?: ModOptions(receivingStaffChat = true, receivingRequests = true, silentMode = false)
    }

    fun fetch(uuid: UUID): ModOptions {
        return Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            if (redis.exists("stark:modOptions:player.$uuid")) {
                val data = redis.hgetAll("stark:modOptions:player.$uuid")
                ModOptions(data["receivingStaffChat"]!!.toBoolean(), data["receivingRequests"]!!.toBoolean(), data["silentMode"]!!.toBoolean())
            }

            ModOptions(receivingStaffChat = true, receivingRequests = true, silentMode = false)
        }
    }

    fun update(uuid: UUID, options: ModOptions) {
        cache[uuid] = options

        val data = mapOf(
                "receivingStaffChat" to options.receivingStaffChat.toString(),
                "receivingRequests" to options.receivingRequests.toString(),
                "silentMode" to options.silentMode.toString()
        )

        Stark.instance.core.redis.runBackboneRedisCommand { redis ->
            redis.hmset("stark:modOptions:player.$uuid", data)
        }
    }

}