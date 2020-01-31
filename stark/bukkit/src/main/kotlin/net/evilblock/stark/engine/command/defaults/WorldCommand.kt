package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.World
import org.bukkit.entity.Player

object WorldCommand {
    @Command(["world"], permission = "essentials.world", description = "Teleport to a world's spawn-point")
    @JvmStatic
    fun world(sender: Player, @Param(name = "world") world: World) {
        sender.teleport(world.spawnLocation.clone().add(0.5, 0.0, 0.5))
    }
}