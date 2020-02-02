package cc.fyre.hub.menu

import cc.fyre.hub.HubLang
import cc.fyre.stark.core.server.Server
import cc.fyre.stark.engine.menu.Button
import cc.fyre.stark.util.BungeeUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

class ServerTypeButton(private val optionalServer: Optional<Server>, private val meta: ServerTypeMeta) : Button() {

    override fun getMaterial(player: Player): Material? {
        return if (optionalServer.isPresent) {
            val server = optionalServer.get()

            if (server.isOnline()) {
                meta.material
            } else {
                Material.REDSTONE_BLOCK
            }
        } else {
            Material.REDSTONE_BLOCK
        }
    }

    override fun getName(player: Player): String? {
        return if (optionalServer.isPresent) {
            val server = optionalServer.get()

            if (server.isOnline()) {
                "${ChatColor.GREEN}${ChatColor.BOLD}${server.serverName}"
            } else {
                "${ChatColor.RED}${ChatColor.BOLD}${server.serverName}"
            }
        } else {
            "${ChatColor.RED}${ChatColor.BOLD}${meta.fallbackName}"
        }
    }

    override fun getDescription(player: Player): List<String>? {
        val description = arrayListOf<String>()
        description.add("")

        for (line in meta.description) {
            description.add(line)
        }

        description.add("")

        if (optionalServer.isPresent) {
            val server = optionalServer.get()

            if (server.isOnline()) {
                description.add(0, "${ChatColor.YELLOW}Players: ${ChatColor.WHITE}(${server.playerCount}/${server.maxSlots})")

                if (server.whitelisted) {
                    description.add("${ChatColor.YELLOW}This server is whitelisted.")
                } else {
                    description.add("${ChatColor.GRAY}${HubLang.LEFT_ARROW_NAKED} ${ChatColor.YELLOW}Click to join the queue! ${ChatColor.GRAY}${HubLang.RIGHT_ARROW_NAKED}")
                }
            } else {
                description.add("${ChatColor.YELLOW}This server is offline.")
            }
        } else {
            description.add("${ChatColor.YELLOW}This server is offline.")
        }

        return description
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (!optionalServer.isPresent || !optionalServer.get().isOnline()) {
            player.sendMessage("${ChatColor.YELLOW}That server is offline.")
        } else {
            val server = optionalServer.get()

            if (server.whitelisted) {
                player.sendMessage("${ChatColor.YELLOW}That server is whitelisted.")
            } else {
                BungeeUtil.sendToServer(player, server.serverName)
            }
        }
    }

}