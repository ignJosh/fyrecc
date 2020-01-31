package net.evilblock.stark.rank

import net.evilblock.stark.Stark
import net.evilblock.stark.core.rank.Rank
import net.evilblock.stark.engine.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.stream.Collectors

class RankParameterType : ParameterType<Rank?> {

    override fun transform(sender: CommandSender, source: String): Rank? {
        val rank = Stark.instance.core.rankHandler.getById(source.toLowerCase())

        if (rank == null) {
            sender.sendMessage(ChatColor.RED.toString() + "Rank named " + source + " doesn't exist")
        }

        return rank
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return Stark.instance.core.rankHandler.getRanks().stream().map<String> { it.displayName }.collect(Collectors.toList())
    }

}