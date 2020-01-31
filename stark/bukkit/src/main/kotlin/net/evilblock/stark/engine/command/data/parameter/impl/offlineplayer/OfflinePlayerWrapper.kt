package net.evilblock.stark.engine.command.data.parameter.impl.offlineplayer

import net.evilblock.stark.Stark
import net.evilblock.stark.util.Callback
import net.evilblock.stark.util.Reflections
import java.util.UUID
import org.bukkit.entity.Player
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable

class OfflinePlayerWrapper(private var source: String) {

    var uniqueId: UUID? = null
    var name: String? = null

    fun loadAsync(callback: Callback<Player?>) {
        object : BukkitRunnable() {
            override fun run() {
                val player = this@OfflinePlayerWrapper.loadSync()
                object : BukkitRunnable() {
                    override fun run() {
                        callback.callback(player)
                    }
                }.runTask(Stark.instance)
            }
        }.runTaskAsynchronously(Stark.instance)
    }

    fun loadSync(): Player? {
        if ((this.source[0] == '\"' || this.source[0] == '\'') && (this.source[this.source.length - 1] == '\"' || this.source[this.source.length - 1] == '\'')) {
            this.source = this.source.replace("'", "").replace("\"", "")
            this.uniqueId = Stark.instance.core.uuidCache.uuid(this.source)

            if (this.uniqueId == null) {
                this.name = this.source
                return null
            }

            this.name = Stark.instance.core.uuidCache.name(this.uniqueId!!)

            if (Bukkit.getPlayer(this.uniqueId) != null) {
                return Bukkit.getPlayer(this.uniqueId)
            }

            if (!Bukkit.getOfflinePlayer(this.uniqueId).hasPlayedBefore()) {
                return null
            }

            val server = Reflections.getMinecraftServer()!!
            val world = Reflections.getMethod(server::class.java, "getWorldServer", Int::class.java)!!.invoke(server, 0)!!
            val gameProfile = Reflections.createGameProfile(this.uniqueId!!, this.name!!)
            val playerInteractManager = Reflections.getConstructor(Reflections.PLAYER_INTERACT_MANAGER_CLASS, Reflections.WORLD_CLASS)!!.newInstance(world)
            val entity = ENTITY_PLAYER_CONSTRUCTOR.newInstance(server, world, gameProfile, playerInteractManager)!!
            val player = Reflections.callMethod(entity, "getBukkitEntity") as Player
            player.loadData()
            return player
        } else {
            if (Bukkit.getPlayer(this.source) != null) {
                return Bukkit.getPlayer(this.source)
            }

            this.uniqueId = Stark.instance.core.uuidCache.uuid(this.source)

            if (this.uniqueId == null) {
                this.name = this.source
                return null
            }

            this.name = Stark.instance.core.uuidCache.name(this.uniqueId!!)

            if (Bukkit.getPlayer(this.uniqueId) != null) {
                return Bukkit.getPlayer(this.uniqueId)
            }

            if (!Bukkit.getOfflinePlayer(this.uniqueId).hasPlayedBefore()) {
                return null
            }

            val server = Reflections.getMinecraftServer()!!
            val world = Reflections.getMethod(server::class.java, "getWorldServer", Int::class.java)!!.invoke(server, 0)!!
            val gameProfile = Reflections.createGameProfile(this.uniqueId!!, this.name!!)
            val playerInteractManager = Reflections.getConstructor(Reflections.PLAYER_INTERACT_MANAGER_CLASS, Reflections.WORLD_CLASS)!!.newInstance(world)
            val entity = ENTITY_PLAYER_CONSTRUCTOR.newInstance(server, world, gameProfile, playerInteractManager)!!
            val player = Reflections.callMethod(entity, "getBukkitEntity") as Player
            player.loadData()
            return player
        }
    }

    companion object {
        private val ENTITY_PLAYER_CONSTRUCTOR = Reflections.getConstructor(
                Reflections.getNMSClass("EntityPlayer")!!,
                Reflections.MINECRAFT_SERVER_CLASS,
                Reflections.WORLD_SERVER_CLASS,
                Reflections.getGameProfileClass(),
                Reflections.PLAYER_INTERACT_MANAGER_CLASS
        )!!
    }

}