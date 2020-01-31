package net.evilblock.stark.profile.grant.command

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.profile.grant.menu.GrantsMenu
import org.bukkit.entity.Player

object GrantsCommand {
    @Command(["grants"], description = "View a player's grants", permission = "stark.grants", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("target") target: BukkitProfile) {
        GrantsMenu(target).openMenu(player)
    }
}