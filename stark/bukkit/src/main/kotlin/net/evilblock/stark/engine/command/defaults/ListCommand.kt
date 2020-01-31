package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object ListCommand {
    @Command(["who", "list"])
    @JvmStatic
    fun execute(sender: CommandSender) {
        sender.sendMessage(StringUtils.join(Stark.instance.core.rankHandler.getRanks().filter { rank -> !rank.hidden }.sortedBy { it.displayOrder }.map { "${ChatColor.translateAlternateColorCodes('&', it.playerListPrefix)}${it.displayName}" }, "${ChatColor.GRAY}, "))
        sender.sendMessage("${ChatColor.GRAY}(${Bukkit.getOnlinePlayers().size}/${Bukkit.getMaxPlayers()}) [${ChatColor.RESET}${StringUtils.join(Bukkit.getOnlinePlayers().map { Stark.instance.core.getProfileHandler().getByUUID(it.uniqueId) }.sortedBy { it!!.getRank().displayOrder }.map { ChatColor.RESET.toString() + it!!.getPlayerListName() }, "${ChatColor.GRAY}, ")}${ChatColor.GRAY}]")
    }
}