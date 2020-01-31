package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.core.util.mojanguser.MojangUser
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object IgnoreCommand {
    @Command(["ignore"], description = "Start ignoring a player. You won't receive private messages from them or see their public chat messages", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("player") target: MojangUser) {
        if (player.uniqueId == target.uuid) {
            player.sendMessage("${ChatColor.RED}You can't ignore yourself.")
            return
        }

        if (Stark.instance.messagingManager.isIgnored(player.uniqueId, target.uuid)) {
            player.sendMessage("${ChatColor.RED}You are already ignoring ${ChatColor.WHITE}${target.username}${ChatColor.RED}.")
            return
        }

        Stark.instance.messagingManager.addToIgnoreList(player.uniqueId, target.uuid)

        player.sendMessage("${ChatColor.YELLOW}Now ignoring ${ChatColor.WHITE}${target.username}${ChatColor.YELLOW}.")
    }
}