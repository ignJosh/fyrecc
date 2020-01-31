package net.evilblock.stark.profile.punishment.menu

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.menu.Button
import net.evilblock.stark.engine.menu.Menu
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class SelectPunishmentTypeMenu(private val target: BukkitProfile) : Menu("${ChatColor.BLUE}Punishments - ${Stark.instance.core.uuidCache.name(target.uuid)}") {

    override fun getButtons(player: Player): Map<Int, Button> {
        val buttons = hashMapOf<Int, Button>()

        if (player.hasPermission("stark.punishment.BLACKLIST.view")) {
            buttons[10] = SelectPunishmentTypeButton(target, ProfilePunishmentType.BLACKLIST)
            buttons[12] = SelectPunishmentTypeButton(target, ProfilePunishmentType.BAN)
            buttons[14] = SelectPunishmentTypeButton(target, ProfilePunishmentType.MUTE)
            buttons[16] = SelectPunishmentTypeButton(target, ProfilePunishmentType.WARN)
        } else {
            buttons[11] = SelectPunishmentTypeButton(target, ProfilePunishmentType.BAN)
            buttons[13] = SelectPunishmentTypeButton(target, ProfilePunishmentType.MUTE)
            buttons[15] = SelectPunishmentTypeButton(target, ProfilePunishmentType.WARN)
        }

        buttons[26] = Button.placeholder(Material.AIR)
        return buttons
    }

}