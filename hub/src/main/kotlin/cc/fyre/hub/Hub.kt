package cc.fyre.hub

import cc.fyre.hub.cosmetic.CosmeticListeners
import cc.fyre.hub.cosmetic.Cosmetics
import cc.fyre.hub.cosmetic.CosmeticsTickRunnable
import cc.fyre.hub.listener.HubListeners
import cc.fyre.hub.listener.PreventionListeners
import cc.fyre.hub.mongo.Mongo
import cc.fyre.hub.mongo.MongoCredentials
import cc.fyre.hub.scoreboard.HubScoreGetter
import org.bukkit.entity.ExperienceOrb
import org.bukkit.entity.Item
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Hub : JavaPlugin() {

    lateinit var mongo: Mongo
    lateinit var cosmetics: Cosmetics

    override fun onEnable() {
        instance = this

        saveDefaultConfig()
        loadDatabase()
        loadEngineAdapters()
        registerListeners()

        cosmetics = Cosmetics()

        server.scheduler.runTaskTimerAsynchronously(this, CosmeticsTickRunnable(), 1L, 1L)

        cleanupWorld()
    }

    private fun loadDatabase() {
        val builder = MongoCredentials.Builder()
                .host(config.getString("Mongo.Host"))
                .port(config.getInt("Mongo.Port"))

        if (config.contains("Mongo.Authentication.Username")) {
            builder.username(config.getString("Mongo.Authentication.Username"))
        }

        if (config.contains("Mongo.Authentication.Password")) {
            builder.password(config.getString("Mongo.Authentication.Password"))
        }

        mongo = Mongo("hub")
        mongo.load(builder.build())
    }

    private fun loadEngineAdapters() {
        Stark.instance.tabEngine.layoutProvider = null

        Stark.instance.scoreboardEngine.configuration = ScoreboardConfiguration(
                TitleGetter.forStaticString("&6&lKihar &f&lNetwork"),
                HubScoreGetter()
        )
    }

    private fun registerListeners() {
        val pluginManager = server.pluginManager
        pluginManager.registerEvents(HubListeners(), this)
        pluginManager.registerEvents(CosmeticListeners(), this)
        pluginManager.registerEvents(PreventionListeners(), this)
    }

    private fun cleanupWorld() {
        server.worlds[0].time = 4000

        for (entity in server.worlds[0].entities) {
            if (entity is Player) {
                continue
            }

            if (entity is LivingEntity || entity is Item || entity is ExperienceOrb) {
                entity.remove()
            }
        }
    }

    companion object {
        lateinit var instance: Hub
    }

}