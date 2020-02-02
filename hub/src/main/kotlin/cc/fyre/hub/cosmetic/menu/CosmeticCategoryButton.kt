package cc.fyre.hub.cosmetic.menu

import cc.fyre.hub.cosmetic.CosmeticCategory
import cc.fyre.stark.engine.menu.Button
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class CosmeticCategoryButton(private val category: CosmeticCategory) : Button() {

    override fun getMaterial(player: Player): Material? {
        return category.getIcon().itemType
    }

    override fun getDamageValue(player: Player): Byte {
        return category.getIcon().data
    }

    override fun getName(player: Player): String? {
        return "${ChatColor.YELLOW}${ChatColor.BOLD}${category.getName()}"
    }

    override fun getDescription(player: Player): List<String>? {
        val description = arrayListOf<String>()
        description.add("")

        val cosmetics = category.getCosmetics()

        cosmetics.forEach {
            if (player.hasPermission(it.getPermission())) {
                description.add("${ChatColor.GRAY}* ${ChatColor.GREEN}${it.getName()}")
            } else {
                description.add("${ChatColor.GRAY}* ${ChatColor.RED}${it.getName()}")
            }
        }

        description.add("")
        description.add("${ChatColor.YELLOW}Click to browse this category.")

        return description
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        CosmeticsMenu(category).openMenu(player)
    }

}