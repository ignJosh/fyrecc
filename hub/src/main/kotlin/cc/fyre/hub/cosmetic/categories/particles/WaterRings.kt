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

class WaterRings : Cosmetic {

    private val ticks = hashMapOf<UUID, Int>()

    override fun getIcon(): ItemStack {
        return ItemStack(Material.RECORD_12)
    }

    override fun getName(): String {
        return "Water Rings"
    }

    override fun getDescription(): List<String> {
        return arrayListOf(
                "&6Surround yourself with",
                "&6rings made of water."
        )
    }

    override fun getPermission(): String {
        return "rank.premium"
    }

    override fun onTick(player: Player) {
        var ticks = this.ticks.putIfAbsent(player.uniqueId, 0) ?: 0

        if (ticks >= 40) {
            ticks = -1
        }

        this.ticks[player.uniqueId] = ticks + 1

        val location = player.location.clone().add(0.1, 0.0, 0.1)
        val angle = ticks * ((2 * Math.PI) / 40)
        val cos = Math.cos(angle)
        val sin = Math.sin(angle)

        val particleMetaList = arrayListOf<ParticleMeta>()

        particleMetaList.add(ParticleMeta(
                location.clone().add(1.0 * cos, 0.5 + (1.0 * cos), 1.0 * sin),
                Effect.WATERDRIP
        ))

        particleMetaList.add(ParticleMeta(
                location.clone().add(1.0 * cos, 1.0 + (1.0 * cos), 1.0 * sin),
                Effect.WATERDRIP
        ))

        particleMetaList.add(ParticleMeta(
                location.clone().add(1.0 * cos, 1.5 + (1.0 * cos), 1.0 * sin),
                Effect.WATERDRIP
        ))

        ParticleUtil.sendsParticleToAll(*particleMetaList.toTypedArray())

//        val xSlantedRingLocation = location.clone().add(1.1 * cos, 0.9 + (0.55 * cos), 1.1 * sin)
//        val zSlantedRingLocation = location.clone().add(1.1 * cos, 0.9 + (1.1 * sin), 1.1 * sin)

//            ParticleUtil.sendsParticleToAll(ParticleMeta(verticalRingLocation, Effect.COLOURED_DUST, 255.0F, 0.0F, 0.0F, 1.0F, 0))
//            ParticleUtil.sendsParticleToAll(ParticleMeta(horizontalRingLocation, Effect.COLOURED_DUST, 255.0F, 0.0F, 0.0F, 1.0F, 0))
//            ParticleUtil.sendsParticleToAll(ParticleMeta(xSlantedRingLocation, Effect.COLOURED_DUST, 255.0F, 0.0F, 0.0F, 1.0F, 0))
//            ParticleUtil.sendsParticleToAll(ParticleMeta(zSlantedRingLocation, Effect.COLOURED_DUST, 255.0F, 0.0F, 0.0F, 1.0F, 0))
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        ticks.remove(event.player.uniqueId)
    }

}