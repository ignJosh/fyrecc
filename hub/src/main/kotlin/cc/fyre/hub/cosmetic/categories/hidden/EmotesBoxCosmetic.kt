package cc.fyre.hub.cosmetic.categories.hidden

import cc.fyre.hub.HubItems
import cc.fyre.hub.cosmetic.Cosmetic
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * Gives a player the [cc.fyre.hub.HubItems.EMOTE_BOX] item,
 * which opens a menu that allows players to display emotes.
 */
class EmotesBoxCosmetic : Cosmetic {

    override fun getIcon(): ItemStack {
        return ItemStack(Material.CHEST)
    }

    override fun getName(): String {
        return "Emotes Box"
    }

    override fun getDescription(): List<String> {
        return emptyList()
    }

    override fun getPermission(): String {
        return "hub.cosmetics.emote-box"
    }

    override fun canBeToggled(): Boolean {
        return false
    }

    override fun onEnable(player: Player) {
        player.inventory.setItem(7, HubItems.EMOTE_BOX)
    }

}