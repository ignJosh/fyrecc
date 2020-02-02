package cc.fyre.hub.cosmetic.menu

import cc.fyre.hub.Hub
import cc.fyre.hub.cosmetic.Cosmetic
import cc.fyre.hub.cosmetic.categories.wearables.RankArmourCosmetic
import cc.fyre.stark.engine.menu.Button
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.LeatherArmorMeta

class CosmeticButton(private val cosmetic: Cosmetic) : Button() {

    override fun getMaterial(player: Player): Material? {
        return cosmetic.getIcon().type
    }

    override fun getDamageValue(player: Player): Byte {
        return cosmetic.getIcon().data.data
    }

    override fun applyMetadata(player: Player, itemMeta: ItemMeta): ItemMeta? {
        if (cosmetic is RankArmourCosmetic) {
            (itemMeta as LeatherArmorMeta).color = cosmetic.color
        }

        return itemMeta
    }

    override fun getName(player: Player): String? {
        return if (player.hasPermission(cosmetic.getPermission())) {
            val color = if (Hub.instance.cosmetics.isCosmeticEnabled(player, cosmetic)) {
                ChatColor.GREEN.toString()
            } else {
                ChatColor.RED.toString()
            }

            color + ChatColor.BOLD + cosmetic.getName()
        } else {
            "${ChatColor.RED}${ChatColor.BOLD}" + cosmetic.getName()
        }
    }

    override fun getDescription(player: Player): List<String>? {
        val description = arrayListOf<String>()
        description.add("")

        if (player.hasPermission(cosmetic.getPermission())) {
            for (line in cosmetic.getDescription()) {
                description.add(line)
            }

            description.add("")
            description.add("${ChatColor.YELLOW}Click to toggle this cosmetic.")
        } else {
            description.add("${ChatColor.RED}${ChatColor.BOLD}COSMETIC LOCKED")
            description.add("${ChatColor.RED}You don't have access to")
            description.add("${ChatColor.RED}this cosmetic. To get access")
            description.add("${ChatColor.RED}purchase a rank on our store.")
        }

        return description
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (player.hasPermission(cosmetic.getPermission())) {
            Hub.instance.cosmetics.toggleCosmetic(player, cosmetic)
        }
    }

}