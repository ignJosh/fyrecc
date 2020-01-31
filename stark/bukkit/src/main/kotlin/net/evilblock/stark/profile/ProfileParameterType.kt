package net.evilblock.stark.profile

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.ArrayList

class ProfileParameterType : ParameterType<BukkitProfile?> {

    override fun transform(sender: CommandSender, source: String): BukkitProfile? {
        val profile = Stark.instance.core.getProfileHandler().fetchProfileByUsername(source)

        if (profile == null) {
            sender.sendMessage(ChatColor.RED.toString() + "A player by the name " + source + " couldn't be found.")
            return null
        }

        return profile
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        Bukkit.getOnlinePlayers().forEach { player ->
            // TODO: add support for visibility engine
            if (sender.canSee(player)) {
                completions.add(player.name)
            }
        }

        return completions
    }

}