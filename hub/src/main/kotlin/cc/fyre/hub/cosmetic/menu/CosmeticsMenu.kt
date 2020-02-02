package cc.fyre.hub.cosmetic.menu

import cc.fyre.hub.cosmetic.CosmeticCategory
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import org.bukkit.Material
import org.bukkit.entity.Player

class CosmeticsMenu(private val category: CosmeticCategory) : Menu() {

    init {
        placeholder = true
        updateAfterClick = true
    }

    override fun getTitle(player: Player): String {
        return "Cosmetics : ${category.getName()}"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        var slotIndex = 0

        category.getCosmetics().forEach { cosmetic ->
            if (cosmetic.canBeToggled()) {
                buttons[slots[slotIndex++]] = CosmeticButton(cosmetic)
            }
        }

        slotIndex--

        // fill remaining row slots
        for (i in 1 .. 8) {
            if (!slots.contains(slots[slotIndex] + i)) {
                break
            }

            buttons[slots[slotIndex] + i] = Button.placeholder(Material.AIR)
        }

        // make bottom glass border row
        buttons[getSlot(9, Math.ceil((slotIndex + 2) / 9.0).toInt())] = Button.placeholder(Material.STAINED_GLASS_PANE, 15, " ")

        return buttons
    }

    companion object {
        private val slots = arrayListOf<Int>()

        init {
            slots.addAll(10 .. 16)
            slots.addAll(19 .. 25)
            slots.addAll(28 .. 34)
            slots.addAll(37 .. 43)
        }
    }

}