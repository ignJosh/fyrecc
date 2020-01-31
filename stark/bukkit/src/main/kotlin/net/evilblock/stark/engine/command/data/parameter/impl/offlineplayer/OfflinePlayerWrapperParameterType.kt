package net.evilblock.stark.engine.command.data.parameter.impl.offlineplayer

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.ArrayList
import org.bukkit.command.CommandSender
import net.evilblock.stark.engine.command.data.parameter.ParameterType

class OfflinePlayerWrapperParameterType : ParameterType<OfflinePlayerWrapper> {

    override fun transform(sender: CommandSender, source: String): OfflinePlayerWrapper {
        return OfflinePlayerWrapper(source)
    }

    override fun tabComplete(sender: Player, flags: Set<String>, source: String): List<String> {
        val completions = ArrayList<String>()

        for (player in Bukkit.getOnlinePlayers()) {
            completions.add(player.name)
        }

        return completions
    }

}