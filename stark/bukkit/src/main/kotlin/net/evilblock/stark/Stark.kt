package net.evilblock.stark

import com.comphenix.protocol.ProtocolLibrary
import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.google.gson.GsonBuilder
import mkremins.fanciful.FancyMessage
import net.evilblock.stark.availability.AvailabilityHeartbeatRunnable
import net.evilblock.stark.availability.AvailabilityListeners
import net.evilblock.stark.config.ConfigMemory
import net.evilblock.stark.core.StarkCore
import net.evilblock.stark.core.mongo.MongoCredentials
import net.evilblock.stark.core.profile.ProfileHandler
import net.evilblock.stark.core.rank.Rank
import net.evilblock.stark.core.rank.runnable.RankLoadRunnable
import net.evilblock.stark.core.redis.RedisCredentials
import net.evilblock.stark.core.uuid.UUIDCacheLoadRunnable
import net.evilblock.stark.engine.command.CommandHandler
import net.evilblock.stark.engine.command.defaults.*
import net.evilblock.stark.engine.menu.ButtonListeners
import net.evilblock.stark.engine.protocol.InventoryAdapter
import net.evilblock.stark.engine.protocol.LagCheck
import net.evilblock.stark.engine.protocol.PingAdapter
import net.evilblock.stark.messaging.MessagingManager
import net.evilblock.stark.messaging.command.*
import net.evilblock.stark.modsuite.ModSuiteMessageListeners
import net.evilblock.stark.modsuite.command.*
import net.evilblock.stark.modsuite.options.ModOptionsListeners
import net.evilblock.stark.profile.*
import net.evilblock.stark.profile.grant.ProfileGrantListeners
import net.evilblock.stark.profile.grant.command.GrantCommand
import net.evilblock.stark.profile.grant.command.GrantsCommand
import net.evilblock.stark.profile.grant.command.RankCommand
import net.evilblock.stark.profile.punishment.command.create.BanCommand
import net.evilblock.stark.profile.punishment.command.create.BlacklistCommand
import net.evilblock.stark.profile.punishment.command.create.MuteCommand
import net.evilblock.stark.profile.punishment.command.create.TempBanCommand
import net.evilblock.stark.profile.punishment.command.remove.UnbanCommand
import net.evilblock.stark.profile.punishment.command.remove.UnblacklistCommand
import net.evilblock.stark.profile.punishment.command.remove.UnmuteCommand
import net.evilblock.stark.profile.runnable.ProfileHeartbeatRunnable
import net.evilblock.stark.rank.RankParameterType
import net.evilblock.stark.rank.RankCommands
import net.evilblock.stark.reboot.RebootCommands
import net.evilblock.stark.reboot.RebootHandler
import net.evilblock.stark.reboot.RebootListener
import net.evilblock.stark.server.ServerHandler
import net.evilblock.stark.server.ServerSyncRunnable
import net.evilblock.stark.server.command.*
import net.evilblock.stark.server.listener.*
import net.evilblock.stark.util.event.HourEvent
import net.evilblock.stark.util.serialization.*
import net.evilblock.stark.uuid.UUIDListeners
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.util.BlockVector
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Stark : JavaPlugin() {

    lateinit var core: StarkCore<BukkitProfile>

    val serverHandler: ServerHandler = ServerHandler()
    val messagingManager: MessagingManager = MessagingManager()
    val rebootHandler: RebootHandler = RebootHandler()
    val commandHandler: CommandHandler = CommandHandler()

    var enabledAt: Long = -1

    override fun onEnable() {

        instance = this
        enabledAt = System.currentTimeMillis()

        ConfigMemory.load()

        try {
            saveDefaultConfig()

            core = object : StarkCore<BukkitProfile>(logger) {
                val profileHandler = object : ProfileHandler<BukkitProfile>() {
                    override fun createProfileInstance(uuid: UUID): BukkitProfile {
                        return BukkitProfile(uuid)
                    }
                }

                override fun isPrimaryThread(): Boolean {
                    return Bukkit.isPrimaryThread()
                }

                override fun getProfileHandler(): ProfileHandler<BukkitProfile> {
                    return profileHandler
                }
            }

            core.load(config.getString("TimeZone"),
                    getRedisCredentials("Local"),
                    getRedisCredentials("Backbone"),
                    getMongoCredentials())

            if (!core.servers.getServerByName(Bukkit.getServerName()).isPresent) {
                core.servers.loadOrCreateServer(Bukkit.getServerName(), Bukkit.getPort())
            }

            checkForServerMismatch()

            core.globalMessageChannel.registerListener(ModSuiteMessageListeners())
            core.globalMessageChannel.registerListener(ProfileMessageListeners())

            loadEngine()

            registerListeners()
            registerCommands()

            setupHourEvents()

            server.scheduler.runTaskTimerAsynchronously(this, ProfileHeartbeatRunnable(), 20L * 30, 20L * 30)
            server.scheduler.runTaskTimerAsynchronously(this, UUIDCacheLoadRunnable(), 20L * 120 * 2, 20L * 120)
            server.scheduler.runTaskTimerAsynchronously(this, RankLoadRunnable(), 20L * 60, 20L * 60)
            server.scheduler.runTaskTimerAsynchronously(this, AvailabilityHeartbeatRunnable(), 20L * 3, 20L * 3)
            server.scheduler.runTaskTimerAsynchronously(this, ServerSyncRunnable(), 40L, 40L)

            server.messenger.registerOutgoingPluginChannel(this, "BungeeCord")

            logger.info("Finished loading stark in ${((System.currentTimeMillis() - enabledAt) / 1000)}ms")
        } catch (e: Exception) {
            logger.severe("An error occurred on startup")
            e.printStackTrace()
            server.shutdown()
        }

        // hijack logging filter to suppress the server's complaints about commands being dispatched async
        Bukkit.getLogger().setFilter {
            return@setFilter !it.message.contains("Command Dispatched Async")
        }

        // hook into vault
        if (server.pluginManager.getPlugin("Vault") != null) {
            Class.forName(this::class.java.`package`.name + ".hook.VaultHook").getMethod("hook").invoke(null)
        }
    }

    override fun onDisable() {
        core.redis.close()
        core.mongo.close()
    }

    private fun loadEngine() {
        serverHandler.load()
        rebootHandler.load()
        commandHandler.load()

        val pingAdapter = PingAdapter()

        ProtocolLibrary.getProtocolManager().addPacketListener(pingAdapter)
        ProtocolLibrary.getProtocolManager().addPacketListener(InventoryAdapter())

        server.pluginManager.registerEvents(pingAdapter, this)

        LagCheck().runTaskTimerAsynchronously(this, 100L, 100L)
    }

    private fun registerListeners() {
        val pm = Bukkit.getPluginManager()
        pm.registerEvents(ButtonListeners(), this)
        pm.registerEvents(UUIDListeners(), this)
        pm.registerEvents(AvailabilityListeners(), this)
        pm.registerEvents(ProfileListeners(), this)
        pm.registerEvents(ProfileGrantListeners(), this)
        pm.registerEvents(RebootListener(), this)
        pm.registerEvents(DisallowedCommandsListeners(), this)
        pm.registerEvents(FreezeListeners(), this)
        pm.registerEvents(FrozenServerListeners(), this)
        pm.registerEvents(FrozenPlayerListeners(), this)
        pm.registerEvents(HeadNameListeners(), this)
        pm.registerEvents(ColoredSignListeners(), this)
        pm.registerEvents(TeleportationListeners(), this)
        pm.registerEvents(ModOptionsListeners(), this)
        pm.registerEvents(ChatFilterListeners(), this)
    }

    private fun registerCommands() {
        commandHandler.registerParameterType(BukkitProfile::class.java, ProfileParameterType())
        commandHandler.registerParameterType(Rank::class.java, RankParameterType())

        // punishment commands
        commandHandler.registerClass(BanCommand::class.java)
        commandHandler.registerClass(TempBanCommand::class.java)
        commandHandler.registerClass(UnbanCommand::class.java)
        commandHandler.registerClass(BlacklistCommand::class.java)
        commandHandler.registerClass(UnblacklistCommand::class.java)
        commandHandler.registerClass(MuteCommand::class.java)
        commandHandler.registerClass(UnmuteCommand::class.java)
        commandHandler.registerClass(KickCommand::class.java)

        // essential commands
        commandHandler.registerClass(BroadcastCommand::class.java)
        commandHandler.registerClass(BuildCommand::class.java)
        commandHandler.registerClass(ClearCacheCommand::class.java)
        commandHandler.registerClass(ClearCommand::class.java)
        commandHandler.registerClass(CraftCommand::class.java)
        commandHandler.registerClass(EnchantCommand::class.java)
        commandHandler.registerClass(FeedCommand::class.java)
        commandHandler.registerClass(FlyCommand::class.java)
        commandHandler.registerClass(FreezeCommand::class.java)
        commandHandler.registerClass(GamemodeCommands::class.java)
        commandHandler.registerClass(HeadCommand::class.java)
        commandHandler.registerClass(HealCommand::class.java)
        commandHandler.registerClass(KillCommand::class.java)
        commandHandler.registerClass(ListCommand::class.java)
        commandHandler.registerClass(MoreCommand::class.java)
        commandHandler.registerClass(RenameCommand::class.java)
        commandHandler.registerClass(RepairCommand::class.java)
        commandHandler.registerClass(SetSlotsCommand::class.java)
        commandHandler.registerClass(SetSpawnCommand::class.java)
        commandHandler.registerClass(SpawnerCommand::class.java)
        commandHandler.registerClass(SpeedCommand::class.java)
        commandHandler.registerClass(SudoCommands::class.java)
        commandHandler.registerClass(TeleportationCommands::class.java)
        commandHandler.registerClass(UptimeCommand::class.java)
        commandHandler.registerClass(WorldCommand::class.java)

        // messaging commands
        commandHandler.registerClass(IgnoreCommand::class.java)
        commandHandler.registerClass(IgnoreListClearCommand::class.java)
        commandHandler.registerClass(IgnoreListCommand::class.java)
        commandHandler.registerClass(IgnoreRemoveCommand::class.java)
        commandHandler.registerClass(MessageCommand::class.java)
        commandHandler.registerClass(ReplyCommand::class.java)
        commandHandler.registerClass(SpyCommand::class.java)
        commandHandler.registerClass(ToggleMessagesCommand::class.java)
        commandHandler.registerClass(ToggleSoundsCommand::class.java)

        // staff commands
        commandHandler.registerClass(AltsCommand::class.java)
        commandHandler.registerClass(ReportCommand::class.java)
        commandHandler.registerClass(RequestCommand::class.java)
        commandHandler.registerClass(StaffChatCommand::class.java)
        commandHandler.registerClass(ToggleStaffChatCommand::class.java)
        commandHandler.registerClass(ToggleRequestsCommand::class.java)

        // profile commands
        commandHandler.registerClass(ProfileCommands::class.java)
        commandHandler.registerClass(GrantCommand::class.java)
        commandHandler.registerClass(GrantsCommand::class.java)
        commandHandler.registerClass(RankCommand::class.java)

        // server management commands
        commandHandler.registerClass(RankCommands::class.java)
        commandHandler.registerClass(RebootCommands::class.java)
        commandHandler.registerClass(ClearChatCommand::class.java)
        commandHandler.registerClass(FreezeServerCommand::class.java)
        commandHandler.registerClass(MuteChatCommand::class.java)
        commandHandler.registerClass(SlowChatCommand::class.java)
        commandHandler.registerClass(WhitelistCommands::class.java)
    }

    private fun checkForServerMismatch() {
        if (!core.servers.checkNamePortMatch(Bukkit.getServerName(), Bukkit.getPort())) {
            logger.severe("********************************************************")
            logger.severe("Can't start server because server.properties config doesn't match the bungee config.yml.")
            logger.severe("Make sure the `server-id` value in `server.properties` matches the server-id assigned to")
            logger.severe("this server's port in your bungee config.yml.")
            logger.severe("********************************************************")
            server.pluginManager.disablePlugin(this)
            server.shutdown()
        }
    }

    private fun setupHourEvents() {
        val executor = Executors.newSingleThreadScheduledExecutor(ThreadFactoryBuilder().setNameFormat("stark - Hour Event Thread").setDaemon(true).build())
        val minOfHour = Calendar.getInstance().get(12)
        val minToHour = 60 - minOfHour
        executor.scheduleAtFixedRate({ instance.server.scheduler.runTask(instance) { Bukkit.getPluginManager().callEvent(HourEvent(Calendar.getInstance().get(11))) } }, minToHour.toLong(), 60L, TimeUnit.MINUTES)
    }

    private fun getRedisCredentials(prefix: String): RedisCredentials {
        val builder = RedisCredentials.Builder()
                .host(config.getString("${prefix}Redis.Host"))
                .port(config.getInt("${prefix}Redis.Port"))

        if (config.contains("${prefix}Redis.Password")) {
            builder.password(config.getString("${prefix}Redis.Password"))
        }

        if (config.contains("${prefix}Redis.DbId")) {
            builder.dbId(config.getInt("${prefix}Redis.DbId"))
        }

        return builder.build()
    }

    private fun getMongoCredentials(): MongoCredentials {
        val builder = MongoCredentials.Builder()
                .host(config.getString("Mongo.Host"))
                .port(config.getInt("Mongo.Port"))

        if (config.contains("Mongo.Username")) {
            builder.username(config.getString("Mongo.Username"))
        }

        if (config.contains("Mongo.Password")) {
            builder.password(config.getString("Mongo.Password"))
        }

        return builder.build()
    }

    companion object {

        @JvmStatic
        lateinit var instance: Stark

        @JvmStatic
        val gson = GsonBuilder()
                .registerTypeHierarchyAdapter(PotionEffect::class.java, PotionEffectAdapter())
                .registerTypeHierarchyAdapter(ItemStack::class.java, ItemStackAdapter())
                .registerTypeHierarchyAdapter(Location::class.java, LocationAdapter())
                .registerTypeHierarchyAdapter(org.bukkit.util.Vector::class.java, VectorAdapter())
                .registerTypeAdapter(BlockVector::class.java, BlockVectorAdapter())
                .setPrettyPrinting()
                .serializeNulls()
                .create()

        @JvmStatic
        val plainGson = GsonBuilder()
                .registerTypeHierarchyAdapter(PotionEffect::class.java, PotionEffectAdapter())
                .registerTypeHierarchyAdapter(ItemStack::class.java, ItemStackAdapter())
                .registerTypeHierarchyAdapter(Location::class.java, LocationAdapter())
                .registerTypeHierarchyAdapter(Vector::class.java, VectorAdapter())
                .registerTypeAdapter(BlockVector::class.java, BlockVectorAdapter())
                .serializeNulls()
                .create()

    }

}