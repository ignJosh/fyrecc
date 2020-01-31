package net.evilblock.stark.command

import net.evilblock.stark.Stark
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.plugin.Command

class ReloadCommand : Command("starkbungeerl", "stark.bungee.reload") {

    override fun execute(sender: CommandSender, args: Array<out String>) {
        Stark.instance.reloadConfig()
        sender.sendMessage("${ChatColor.GOLD}Reloaded stark bungee config")
    }

}