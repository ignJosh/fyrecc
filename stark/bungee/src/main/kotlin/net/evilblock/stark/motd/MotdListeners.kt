package net.evilblock.stark.motd

import net.evilblock.stark.Stark
import net.evilblock.stark.core.util.TimeUtils
import net.evilblock.stark.util.ChatUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class MotdListeners : Listener {

    @EventHandler
    fun onProxyPingEvent(event: ProxyPingEvent) {
        val activeMotd = Stark.instance.motdHandler.active

        if (activeMotd != null) {
            var textToTranslate = activeMotd.motd

            if (activeMotd.countdown != null) {
                val current = System.currentTimeMillis()
                val remaining = activeMotd.countdown!! - current

                if (remaining < 0) {
                    textToTranslate = activeMotd.countdownFinishMotd!!
                } else {
                    textToTranslate = textToTranslate.replace("%countdown%", TimeUtils.formatIntoShortString((remaining / 1000).toInt()))
                }
            }

            val description = ChatUtil.color(textToTranslate)
            val descriptionBuilder = ComponentBuilder("")

            description.split(ChatColor.COLOR_CHAR.toString() + "r").forEach {
                descriptionBuilder.append(TextComponent.fromLegacyText(it))
                descriptionBuilder.append(TextComponent(""))
                descriptionBuilder.bold(false)
                descriptionBuilder.italic(false)
                descriptionBuilder.obfuscated(false)
                descriptionBuilder.strikethrough(false)
            }

            val serverPing = event.response
            serverPing.descriptionComponent = ChatUtil.compile(descriptionBuilder.create())
            event.response = serverPing
        }
    }

}