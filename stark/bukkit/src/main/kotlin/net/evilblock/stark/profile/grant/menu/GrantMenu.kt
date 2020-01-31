package net.evilblock.stark.profile.grant.menu

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.menu.Button
import net.evilblock.stark.engine.menu.Menu
import net.evilblock.stark.profile.BukkitProfile
import org.bukkit.entity.Player

class GrantMenu(val target: BukkitProfile) : Menu("Grant ${target.getPlayerListName()}") {

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        for (rank in Stark.instance.core.rankHandler.getRanks().sortedBy { it.displayOrder }) {
            if (rank.default) continue

            if (player.hasPermission("stark.grant") && player.hasPermission("stark.grant." + rank.id)) {
                buttons[buttons.size] = GrantRankButton(rank, target)
            }
        }

        return buttons
    }

}