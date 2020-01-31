package net.evilblock.stark.rank

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object RankCommands {

    @Command(["ranks"], "op")
    @JvmStatic fun execute(sender: CommandSender) {
        for (rank in Stark.instance.core.rankHandler.getRanks().sortedBy { rank -> rank.displayOrder }) {
            sender.sendMessage("${ChatColor.translateAlternateColorCodes('&', rank.gameColor)}${rank.displayName}")
        }
    }

}