package net.evilblock.stark.engine.menu.buttons

import net.evilblock.stark.engine.menu.Button
import net.evilblock.stark.util.Callback
import org.bukkit.Material
import org.bukkit.event.inventory.ClickType
import org.bukkit.entity.Player
import java.util.ArrayList

class BackButton(private val callback: Callback<Player>) : Button() {

    override fun getMaterial(player: Player): Material? {
        return Material.BED
    }

    override fun getName(player: Player): String? {
        return "§cGo back"
    }

    override fun getDescription(player: Player): List<String>? {
        return ArrayList()
    }

    override fun clicked(player: Player, i: Int, clickType: ClickType) {
        playNeutral(player)
        player.closeInventory()

        callback.callback(player)
    }

}