package net.evilblock.stark.core.server

import nig.cock.nigger.message.handler.IncomingMessageHandler
import nig.cock.nigger.message.listener.MessageListener
import net.evilblock.stark.core.StarkCore
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

object ServerMessageListeners : MessageListener {

    @IncomingMessageHandler("GLOBAL_COUNT")
    fun onGlobalCount(json: JsonObject) {
        StarkCore.instance.servers.globalCount = json["globalCount"].asInt
    }

    @IncomingMessageHandler("SERVER_GROUP_UPDATE")
    fun onServerGroupUpdate(json: JsonObject) {
        val map = GSON.fromJson<Map<String, String>>(json, TYPE) as Map<String, String>

        val optionalGroup = StarkCore.instance.servers.getServerGroupByName(map["groupName"]!!)

        if (optionalGroup.isPresent) {
            optionalGroup.get().configuration = GSON.fromJson(map["configuration"]!!, JsonObject::class.java)
        } else {
            val group = ServerGroup(map)
            StarkCore.instance.servers.groups[group.groupName] = group
        }
    }

    @IncomingMessageHandler("SERVER_UPDATE")
    fun onServerUpdate(json: JsonObject) {
        val map = GSON.fromJson<Map<String, String>>(json, TYPE) as Map<String, String>

        val optionalServer = StarkCore.instance.servers.getServerByName(map["serverName"]!!)

        if (optionalServer.isPresent) {
            val server = optionalServer.get()
            server.lastHeartbeat = map["lastHeartbeat"]!!.toLong()
            server.currentUptime = map["currentUptime"]!!.toLong()
            server.currentTps = map["currentTps"]!!.toDouble()
            server.playerCount = map["playerCount"]!!.toInt()
            server.maxSlots = map["maxSlots"]!!.toInt()
            server.whitelisted = map["whitelisted"]!!.toBoolean()
        } else {
            StarkCore.instance.servers.loadOrCreateServer(map["serverName"]!!, map["serverPort"]!!.toInt())
        }
    }

        private val GSON = GsonBuilder().create()
        private val TYPE = TypeToken.getParameterized(Map::class.java, String::class.java, String::class.java).rawType

}