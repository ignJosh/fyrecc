package net.evilblock.stark.messaging.command

import net.evilblock.stark.Stark
import net.evilblock.stark.core.util.mojanguser.MojangUser
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object IgnoreRemoveCommand {
    @Command(["ignore remove"], description = "Stop ignoring a player", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("player") target: MojangUser) {
        if (!Stark.instance.messagingManager.isIgnored(player.uniqueId, target.uuid)) {
            player.sendMessage("${ChatColor.RED}You aren't ignoring ${ChatColor.WHITE}${target.username}${ChatColor.RED}.")
            return
        }

        Stark.instance.messagingManager.removeFromIgnoreList(player.uniqueId, target.uuid)

        player.sendMessage("${ChatColor.YELLOW}You are no longer ignoring ${ChatColor.WHITE}${target.username}${ChatColor.YELLOW}.")
    }
}