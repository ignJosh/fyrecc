package cc.fyre.hub

import cc.fyre.stark.util.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material

object HubItems {

    val SELECTOR = ItemBuilder.of(Material.WATCH)
            .name("${ChatColor.YELLOW}${ChatColor.BOLD}Server Selector")
            .build()

    val COSMETICS = ItemBuilder.of(Material.FEATHER)
            .name("${ChatColor.LIGHT_PURPLE}${ChatColor.BOLD}Cosmetics")
            .build()

    val ENDER_BUTT = ItemBuilder.of(Material.ENDER_PEARL)
            .name("${ChatColor.YELLOW}${ChatColor.BOLD}Enderbutt")
            .build()

    val EMOTE_BOX = ItemBuilder.of(Material.CHEST)
            .name("${ChatColor.GREEN}${ChatColor.BOLD}Emote Box")
            .build()

}