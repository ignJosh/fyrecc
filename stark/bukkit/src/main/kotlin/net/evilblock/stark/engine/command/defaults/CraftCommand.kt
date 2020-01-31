package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import org.bukkit.entity.Player

object CraftCommand {
    @Command(["craft"], permission = "essentials.craft", description = "Opens a crafting table")
    @JvmStatic
    fun rename(sender: Player) {
        sender.openWorkbench(sender.location, true)
    }
}