package net.evilblock.stark.util

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent

object ChatUtil {

    fun color(string: String): String {
        return ChatColor.translateAlternateColorCodes('&', string)
    }

    fun compile(components: Array<BaseComponent>): BaseComponent {
        val root = components[0]
        var current = components[0]

        for (i in 1 until components.size) {
            current.addExtra(components[i])
            current = components[i]
        }

        return root
    }

}