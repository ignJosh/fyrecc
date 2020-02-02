package cc.fyre.hub.cosmetic.menu

import cc.fyre.hub.cosmetic.categories.hidden.emote.Emotes
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import org.bukkit.entity.Player

class EmoteBoxMenu : Menu() {

    override fun getTitle(player: Player): String {
        return "Emote Box"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        var i = 0
        for (emote in Emotes.emotes) {
            buttons[i++] = EmoteButton(emote)
        }

        return buttons
    }

}