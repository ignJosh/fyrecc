package cc.fyre.hub.cosmetic.menu

import cc.fyre.hub.cosmetic.categories.hidden.emote.Emote
import cc.fyre.stark.engine.menu.Button
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class EmoteButton(private val emote: Emote) : Button() {

    override fun getMaterial(player: Player): Material? {
        return emote.getIcon().itemType
    }

    override fun getDamageValue(player: Player): Byte {
        return emote.getIcon().data
    }

    override fun getName(player: Player): String? {
        return "${ChatColor.YELLOW}${emote.getDisplayName()}"
    }

    override fun getDescription(player: Player): List<String>? {
        val description = arrayListOf<String>()
        description.add("")

        for (line in emote.getDescription()) {
            description.add(line)
        }

        description.add("")
        description.add("${ChatColor.YELLOW}Click to perform this emote.")

        return description
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()
        emote.playEffect(player.eyeLocation.clone().add(player.location.direction))
    }

}