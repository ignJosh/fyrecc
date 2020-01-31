package net.evilblock.stark.command

import net.evilblock.stark.Stark
import net.evilblock.stark.util.ChatUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.connection.ProxiedPlayer
import net.md_5.bungee.api.plugin.Command

class HubCommand : Command("hub", null, "lobby") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        if (sender is ProxiedPlayer) {
            if (args.isEmpty()) {
                if (Stark.instance.isHubServer(sender.server.info)) {
                    sender.sendMessage("${ChatColor.RED}You're already connected to a hub server.")
                    return
                }

                val hub = Stark.instance.syncHandler.findNextBestHubServer(sender)

                if (hub != null) {
                    sender.connect(hub)
                } else {
                    sender.sendMessage("${ChatColor.RED}Couldn't find an open hub server for you to join.")
                }
            }
        }
    }

}