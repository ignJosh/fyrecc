package cc.fyre.hub.cosmetic.categories.particles

import cc.fyre.hub.cosmetic.Cosmetic
import cc.fyre.hub.util.ParticleMeta
import cc.fyre.hub.util.ParticleUtil
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class LavaRings : Cosmetic {

    private val ticks = hashMapOf<UUID, Int>()

    override fun getIcon(): ItemStack {
        return ItemStack(Material.RECORD_4)
    }

    override fun getName(): String {
        return "Lava Rings"
    }

    override fun getDescription(): List<String> {
        return arrayListOf(
                "&6Surround yourself with",
                "&6rings made of lava."
        )
    }

    override fun getPermission(): String {
        return "rank.platinum"
    }

    override fun onTick(player: Player) {
        var ticks = this.ticks.putIfAbsent(player.uniqueId, 0) ?: 0

        if (ticks >= 32) {
            ticks = -1
        }

        this.ticks[player.uniqueId] = ticks + 1

        val location = player.location.clone().add(0.1, 0.0, 0.1)
        val angle = ticks * ((2 * Math.PI) / 32)
        val cos = Math.cos(angle)
        val sin = Math.sin(angle)

        val bottomRingLocation = location.clone().add(0.8 * cos, 0.6, 0.8 * sin)
        val topRingLocation = location.clone().add(0.8 * cos, 1.4, 0.8 * sin)

        for (i in 0 until 5) {
            ParticleUtil.sendsParticleToAll(ParticleMeta(bottomRingLocation, Effect.LAVADRIP))
            ParticleUtil.sendsParticleToAll(ParticleMeta(topRingLocation, Effect.LAVADRIP))
        }
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        ticks.remove(event.player.uniqueId)
    }

}