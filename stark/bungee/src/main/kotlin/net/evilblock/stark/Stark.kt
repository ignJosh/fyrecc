package net.evilblock.stark

import nig.cock.nigger.Pidgin
import net.evilblock.stark.availability.AvailabilityHeartbeatRunnable
import net.evilblock.stark.availability.AvailabilityListeners
import net.evilblock.stark.command.HubCommand
import net.evilblock.stark.command.ReloadCommand
import net.evilblock.stark.command.SetMotdStateCommand
import net.evilblock.stark.core.StarkCore
import net.evilblock.stark.core.mongo.MongoCredentials
import net.evilblock.stark.core.profile.ProfileHandler
import net.evilblock.stark.core.redis.RedisCredentials
import net.evilblock.stark.core.uuid.UUIDCacheLoadRunnable
import net.evilblock.stark.motd.MotdHandler
import net.evilblock.stark.motd.MotdListeners
import net.evilblock.stark.profile.ProfileCheckRunnable
import net.evilblock.stark.profile.ProfileListeners
import net.evilblock.stark.profile.ProfileMessageListeners
import net.evilblock.stark.profile.ProxyProfile
import net.evilblock.stark.staff.StaffListeners
import net.evilblock.stark.staff.StaffMessageListeners
import net.evilblock.stark.sync.SyncHandler
import net.evilblock.stark.sync.SyncListeners
import net.evilblock.stark.sync.SyncMessageListeners
import net.evilblock.stark.sync.runnable.BroadcastCountRunnable
import net.evilblock.stark.sync.runnable.HeartbeatProxyRunnable
import net.evilblock.stark.whitelist.WhitelistListeners
import net.evilblock.stark.whitelist.WhitelistMessageListeners
import com.google.common.io.ByteStreams
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.TimeUnit

class Stark : Plugin() {

    lateinit var configuration: Configuration
    lateinit var core: StarkCore<ProxyProfile>

    var syncHandler: SyncHandler = SyncHandler()
    var motdHandler: MotdHandler = MotdHandler()
    lateinit var proxyMessageChannel: Pidgin

    lateinit var mainThread: Thread

    override fun onEnable() {
        instance = this
        mainThread = Thread.currentThread()

        try {
            saveDefaultConfig()
            loadConfig()

            core = object : StarkCore<ProxyProfile>(proxy.logger) {
                val profileHandler = object : ProfileHandler<ProxyProfile>() {
                    override fun createProfileInstance(uuid: UUID): ProxyProfile {
                        return ProxyProfile(uuid)
                    }
                }

                override fun isPrimaryThread(): Boolean {
                    return Thread.currentThread() == mainThread
                }

                override fun getProfileHandler(): ProfileHandler<ProxyProfile> {
                    return profileHandler
                }
            }

            core.load(configuration.getString("TimeZone"),
                    getRedisCredentials("Local"),
                    getRedisCredentials("Backbone"),
                    getMongoCredentials())

            proxy.servers.values.forEach {
                core.servers.loadOrCreateServer(it.name, it.address.port)
            }

            core.globalMessageChannel.registerListener(ProfileMessageListeners())
            core.globalMessageChannel.registerListener(WhitelistMessageListeners())

            proxyMessageChannel = Pidgin("Stark:PROXY", core.redis.backboneJedisPool!!)
            proxyMessageChannel.registerListener(SyncMessageListeners())
            proxyMessageChannel.registerListener(StaffMessageListeners())

            motdHandler.loadStates()

            registerListeners()
            registerCommands()

            proxy.scheduler.schedule(this, HeartbeatProxyRunnable(), 1, 1, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, BroadcastCountRunnable(), 500, 500, TimeUnit.MILLISECONDS)
            proxy.scheduler.schedule(this, AvailabilityHeartbeatRunnable(), 1, 5, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, ProfileCheckRunnable(), 1, 3, TimeUnit.SECONDS)
            proxy.scheduler.schedule(this, { proxy.scheduler.runAsync(this, UUIDCacheLoadRunnable()) }, 2, 2, TimeUnit.MINUTES)
        } catch (e: Exception) {
            logger.severe("An error occurred on startup")
            e.printStackTrace()
            proxy.stop()
        }
    }

    override fun onDisable() {
        core.redis.close()
        core.mongo.close()
    }

    fun isHubServer(server: ServerInfo): Boolean {
        return server.name.startsWith("Hub-")
    }

    fun isRestrictedHubServer(server: ServerInfo): Boolean {
        return server.name.contains("Hub", ignoreCase = true) && server.name.contains("Restricted", ignoreCase = true)
    }

    private fun saveDefaultConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            try {
                configFile.createNewFile()
                getResourceAsStream("config.yml").use { `is` -> FileOutputStream(configFile).use { os -> ByteStreams.copy(`is`, os) } }
            } catch (e: Throwable) {
                throw RuntimeException("Unable to create configuration file", e)
            }
        }
    }

    private fun loadConfig() {
        configuration = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(dataFolder, "config.yml"))
    }

    fun reloadConfig() {
        loadConfig()
        motdHandler.loadStates()
    }

    private fun registerListeners() {
        proxy.pluginManager.registerListener(this, SyncListeners())
        proxy.pluginManager.registerListener(this, WhitelistListeners())
        proxy.pluginManager.registerListener(this, MotdListeners())
        proxy.pluginManager.registerListener(this, AvailabilityListeners())
        proxy.pluginManager.registerListener(this, ProfileListeners())
        proxy.pluginManager.registerListener(this, StaffListeners())
    }

    private fun registerCommands() {
        proxy.pluginManager.registerCommand(this, ReloadCommand())
        proxy.pluginManager.registerCommand(this, SetMotdStateCommand())
        proxy.pluginManager.registerCommand(this, HubCommand())
    }

    private fun getRedisCredentials(prefix: String): RedisCredentials {
        val builder = RedisCredentials.Builder()
                .host(configuration.getString("${prefix}Redis.Host"))
                .port(configuration.getInt("${prefix}Redis.Port"))

        if (configuration.contains("${prefix}Redis.Password")) {
            builder.password(configuration.getString("${prefix}Redis.Password"))
        }

        if (configuration.contains("${prefix}Redis.DbId")) {
            builder.dbId(configuration.getInt("${prefix}Redis.DbId"))
        }

        return builder.build()
    }

    private fun getMongoCredentials(): MongoCredentials {
        val builder = MongoCredentials.Builder()
                .host(configuration.getString("Mongo.Host"))
                .port(configuration.getInt("Mongo.Port"))

        if (configuration.contains("Mongo.Username")) {
            builder.username(configuration.getString("Mongo.Username"))
        }

        if (configuration.contains("Mongo.Password")) {
            builder.password(configuration.getString("Mongo.Password"))
        }

        return builder.build()
    }

    companion object {
        lateinit var instance: Stark
    }

}