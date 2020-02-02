package cc.fyre.hub.menu

import cc.fyre.stark.Stark
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.engine.menu.Menu
import org.bukkit.Material
import org.bukkit.entity.Player

class ServerSelectorMenu : Menu() {

    init {
        autoUpdate = true
        updateAfterClick = true
    }

    override fun getTitle(player: Player): String {
        return "Select a server to join"
    }

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        buttons[11] = ServerTypeButton(
                Stark.instance.core.servers.getServerByName("Practice-NA"),
                ServerTypeMeta(
                        Material.DIAMOND_SWORD,
                        "Practice-NA",
                        arrayListOf(
                                "&7* &61v1s, 2v2s, Team Fights!",
                                "&7* &6Leaderboards, Clan Wars!",
                                "&7* &6Hosted Events, Party Events!"
                        )
                )
        )

        buttons[13] = ServerTypeButton(
                Stark.instance.core.servers.getServerByName("KitMap"),
                ServerTypeMeta(
                        Material.ENDER_PEARL,
                        "KitMap",
                        arrayListOf(
                                "&7* &65 Minute KOTHs!",
                                "&7* &624/7 PvP, Killstreaks!",
                                "&7* &6Bases, Obstacles!"
                        )
                )
        )

        buttons[15] = ServerTypeButton(
                Stark.instance.core.servers.getServerByName("MineSG"),
                ServerTypeMeta(
                        Material.CHEST,
                        "MineSG",
                        arrayListOf(
                                "&7* &6Solo and Duo Queues!",
                                "&7* &650 Players Per Game!",
                                "&7* &6Last Man Standing Wins!"
                        )
                )
        )

        buttons[26] = Button.placeholder(Material.AIR)

        return buttons
    }

}