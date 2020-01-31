package net.evilblock.stark.engine.command.data.parameter.impl

import net.evilblock.stark.Stark
import net.evilblock.stark.core.util.mojanguser.MojangUser
import net.evilblock.stark.engine.command.data.parameter.ParameterType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class MojangUserParameterType : ParameterType<MojangUser?> {

    override fun transform(sender: CommandSender, source: String): MojangUser? {
        val mojangUser = Stark.instance.core.getProfileHandler().fetchMojangUser(source)

        if (mojangUser == null) {
            sender.sendMessage("${ChatColor.RED}A player by that name doesn't exist.")
        }

        return mojangUser
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        Bukkit.getOnlinePlayers().forEach { target ->
            completions.add(target.name)
        }

        return completions
    }

}