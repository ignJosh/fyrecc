package net.evilblock.stark.core

import nig.cock.nigger.Pidgin
import net.evilblock.stark.core.availability.AvailabilityHandler
import net.evilblock.stark.core.mongo.Mongo
import net.evilblock.stark.core.mongo.MongoCredentials
import net.evilblock.stark.core.profile.Profile
import net.evilblock.stark.core.profile.ProfileHandler
import net.evilblock.stark.core.rank.RankHandler
import net.evilblock.stark.core.rank.RankMessageListeners
import net.evilblock.stark.core.redis.Redis
import net.evilblock.stark.core.redis.RedisCredentials
import net.evilblock.stark.core.server.ServerMessageListeners
import net.evilblock.stark.core.server.Servers
import net.evilblock.stark.core.uuid.UUIDCache
import net.evilblock.stark.core.whitelist.Whitelist
import java.util.*
import java.util.logging.Logger

abstract class StarkCore<T : Profile>(val logger: Logger) {

    lateinit var redis: Redis
    lateinit var mongo: Mongo

    val uuidCache: UUIDCache = UUIDCache()
    val servers: Servers = Servers()
    val rankHandler: RankHandler = RankHandler()
    val availabilityHandler: AvailabilityHandler = AvailabilityHandler()
    val whitelist: Whitelist = Whitelist()

    lateinit var globalMessageChannel: Pidgin

    init {
        instance = this
    }

    fun load(timezone: String, localRedisCredentials: RedisCredentials, backboneRedisCredentials: RedisCredentials, mongoCredentials: MongoCredentials) {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone))

        redis = Redis()
        redis.load(localRedisCredentials, backboneRedisCredentials)

        mongo = Mongo("stark")
        mongo.load(mongoCredentials)

        uuidCache.load()
        servers.initialLoad()
        rankHandler.load()
        whitelist.load()

        getProfileHandler().load()

        globalMessageChannel = Pidgin("Stark:ALL", redis.backboneJedisPool!!)
        globalMessageChannel.registerListener(ServerMessageListeners)
        globalMessageChannel.registerListener(RankMessageListeners)
    }

    abstract fun isPrimaryThread(): Boolean

    abstract fun getProfileHandler(): ProfileHandler<T>

    companion object {
        lateinit var instance: StarkCore<*>

        const val COLOR_CODE_CHAR = '§'
    }

}