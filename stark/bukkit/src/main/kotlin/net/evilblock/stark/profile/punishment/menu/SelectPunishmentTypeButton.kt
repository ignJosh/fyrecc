package net.evilblock.stark.profile.punishment.menu

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.menu.Button
import net.evilblock.stark.profile.BukkitProfile
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import net.evilblock.stark.util.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack

class SelectPunishmentTypeButton(private val target: BukkitProfile, private val punishmentType: ProfilePunishmentType) : Button() {

    override fun getName(player: Player): String? {
        return null
    }

    override fun getDescription(player: Player): List<String>? {
        return null
    }

    override fun getMaterial(player: Player): Material? {
        return null
    }

    override fun getButtonItem(player: Player): ItemStack {
        return ItemBuilder.of(Material.WOOL)
                .name("${ChatColor.translateAlternateColorCodes('&', punishmentType.color)}${punishmentType.name.toLowerCase().capitalize()}s")
                .data(when (punishmentType) {
                    ProfilePunishmentType.BLACKLIST -> 14
                    ProfilePunishmentType.BAN -> 1
                    ProfilePunishmentType.MUTE -> 4
                    ProfilePunishmentType.WARN -> 5
                })
                .build()
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (punishmentType != ProfilePunishmentType.WARN) {
            Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                PunishmentsMenu(target, punishmentType).openMenu(player)
            }
        }
    }

}