package net.evilblock.stark.engine.menu.menus

import net.evilblock.stark.engine.menu.Button
import net.evilblock.stark.engine.menu.Menu
import net.evilblock.stark.util.Callback
import org.bukkit.entity.Player
import net.evilblock.stark.engine.menu.buttons.BooleanButton
import org.bukkit.Material
import java.util.HashMap

class ConfirmMenu(private val title: String, private val callback: Callback<Boolean>) : Menu() {

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = HashMap<Int, Button>()

        for (i in 0..8) {
            when (i) {
                3 -> buttons[i] = BooleanButton(true, callback)
                5 -> buttons[i] = BooleanButton(false, callback)
                else -> buttons[i] = Button.placeholder(Material.STAINED_GLASS_PANE, 14.toByte())
            }
        }

        return buttons
    }

    override fun getTitle(player: Player): String {
        return title
    }

}