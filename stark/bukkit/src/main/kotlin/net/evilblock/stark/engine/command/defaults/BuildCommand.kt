package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

object BuildCommand {
    @Command(["build"], permission = "op")
    @JvmStatic
    fun build(sender: Player) {
        if (sender.hasMetadata("Build")) {
            sender.removeMetadata("Build", Stark.instance)
        } else {
            sender.setMetadata("Build", FixedMetadataValue(Stark.instance, true))
        }

        sender.sendMessage("${ChatColor.YELLOW}You are " + (if (sender.hasMetadata("Build")) "${ChatColor.GREEN}now" else StringBuilder().append(ChatColor.RED).append("no longer").toString()) + "${ChatColor.YELLOW} in build mode.")
    }
}