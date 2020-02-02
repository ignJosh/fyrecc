package cc.fyre.hub.cosmetic.categories

import cc.fyre.hub.cosmetic.Cosmetic
import cc.fyre.hub.cosmetic.CosmeticCategory
import cc.fyre.hub.cosmetic.categories.wearables.RankArmourCosmetic
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.material.MaterialData

class WearablesCosmeticCategory : CosmeticCategory {

    private val cosmetics = arrayListOf(
            RankArmourCosmetic("Owner", "rank.owner", true, Color.fromRGB(156, 10, 8)),
            RankArmourCosmetic("Manager", "rank.manager", true, Color.fromRGB(196, 14, 47)),
            RankArmourCosmetic("Admin", "rank.admin", true, Color.fromRGB(209, 35, 29)),
            RankArmourCosmetic("Mod", "rank.moderator", true, Color.PURPLE),
            RankArmourCosmetic("T-Mod", "rank.trial-mod", true, Color.fromRGB(15, 214, 214)),
            RankArmourCosmetic("Kihar", "rank.kihar", false, Color.fromRGB(0,170,170)),
            RankArmourCosmetic("Platinum", "rank.platinum", false, Color.fromRGB(252, 144, 3)),
            RankArmourCosmetic("Premium", "rank.premium", false, Color.fromRGB(85,85,255)),
            RankArmourCosmetic("Basic", "rank.basic", false, Color.fromRGB(23, 227, 77))
    )

    override fun getIcon(): MaterialData {
        return MaterialData(Material.PUMPKIN)
    }

    override fun getName(): String {
        return "Wearables"
    }

    override fun getCosmetics(): List<Cosmetic> {
        return ArrayList(cosmetics)
    }

}