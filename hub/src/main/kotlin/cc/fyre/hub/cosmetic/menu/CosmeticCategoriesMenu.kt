package cc.fyre.hub.cosmetic.menu

import cc.fyre.hub.Hub
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import org.bukkit.Material
import org.bukkit.entity.Player

class CosmeticCategoriesMenu : Menu() {

    init {
        placeholder = true
        updateAfterClick = true
    }

    override fun getTitle(player: Player): String {
        return "Cosmetics Menu"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        buttons[11] = CosmeticCategoryButton(Hub.instance.cosmetics.categories[0])
        buttons[15] = CosmeticCategoryButton(Hub.instance.cosmetics.categories[1])

        buttons[26] = Button.placeholder(Material.STAINED_GLASS_PANE, 15.toByte(), " ")

        return buttons
    }

}