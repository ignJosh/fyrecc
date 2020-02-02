package cc.fyre.hub.cosmetic.categories

import cc.fyre.hub.cosmetic.Cosmetic
import cc.fyre.hub.cosmetic.CosmeticCategory
import cc.fyre.hub.cosmetic.categories.particles.WaterRings
import cc.fyre.hub.cosmetic.categories.particles.GodModeCosmetic
import cc.fyre.hub.cosmetic.categories.particles.LavaRings
import org.bukkit.Material
import org.bukkit.material.MaterialData

class ParticlesCosmeticCategory : CosmeticCategory {

    private val cosmetics = arrayListOf(
            GodModeCosmetic(),
            LavaRings(),
            WaterRings()
    )

    override fun getIcon(): MaterialData {
        return MaterialData(Material.RECORD_4)
    }

    override fun getName(): String {
        return "Particles"
    }

    override fun getCosmetics(): List<Cosmetic> {
        return ArrayList(cosmetics)
    }

}