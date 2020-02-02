package cc.fyre.hub.cosmetic.categories

import cc.fyre.hub.cosmetic.Cosmetic
import cc.fyre.hub.cosmetic.CosmeticCategory
import cc.fyre.hub.cosmetic.categories.hidden.EmotesBoxCosmetic
import org.bukkit.Material
import org.bukkit.material.MaterialData

class HiddenCosmeticCategory : CosmeticCategory {

    private val cosmetics = arrayListOf<Cosmetic>(
            EmotesBoxCosmetic()
    )

    override fun getIcon(): MaterialData {
        return MaterialData(Material.AIR)
    }

    override fun getName(): String {
        return "Hidden"
    }

    override fun getCosmetics(): List<Cosmetic> {
        return ArrayList(cosmetics)
    }

}