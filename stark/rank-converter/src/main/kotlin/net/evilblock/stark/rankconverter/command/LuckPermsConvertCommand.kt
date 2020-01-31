package net.evilblock.stark.rankconverter.command

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.rankconverter.conversion.LuckPermsRankConverter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object LuckPermsConvertCommand {

    @Command(["rank convert lp"], permission = "op", async = true)
    @JvmStatic
    fun execute(sender: CommandSender) {
        sender.sendMessage("${ChatColor.GREEN}Converting ranks from LuckPerms...")
        LuckPermsRankConverter.convert()
        sender.sendMessage("${ChatColor.GREEN}Done converting!")
    }

}