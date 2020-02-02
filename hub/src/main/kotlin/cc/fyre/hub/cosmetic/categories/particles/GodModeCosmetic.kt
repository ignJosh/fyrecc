package cc.fyre.hub.cosmetic.categories.particles

import cc.fyre.hub.cosmetic.Cosmetic
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Displays particles under the player's feet that makes
 * it seem like the player is flying.
 */
class GodModeCosmetic : Cosmetic {

    override fun getIcon(): ItemStack {
        return ItemStack(Material.FEATHER)
    }

    override fun getName(): String {
        return "God Mode"
    }

    override fun getDescription(): List<String> {
        return arrayListOf(
                "&6Appear as if you're floating",
                "&6on a cloud. (Look down)"
        )
    }

    override fun getPermission(): String {
        return "hub.cosmetics.god-mode"
    }

}