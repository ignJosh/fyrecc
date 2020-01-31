package net.evilblock.stark.profile.punishment.command.view

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.profile.punishment.menu.SelectPunishmentTypeMenu
import org.bukkit.entity.Player

object CheckCommand {
    @Command(["check", "c"], description = "View a player's punishments", permission = "stark.check", async = true)
    @JvmStatic
    fun execute(player: Player, @Param("target") target: BukkitProfile) {
        SelectPunishmentTypeMenu(target).openMenu(player)
    }
}