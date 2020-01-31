package net.evilblock.stark.engine.menu

import net.evilblock.stark.util.SoundCompat
import com.google.common.base.Joiner
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.stream.Collectors

abstract class Button {

    companion object {
        val BAR = "&7&m${StringUtils.repeat("-", 32)}"

        @JvmStatic fun placeholder(material: Material, data: Byte, vararg title: String): Button {
            return placeholder(material, data, Joiner.on(" ").join(title))
        }

        @JvmStatic fun placeholder(material: Material): Button {
            return placeholder(material, " ")
        }

        @JvmStatic fun placeholder(material: Material, title: String): Button {
            return placeholder(material, 0.toByte(), title)
        }

        @JvmStatic fun placeholder(material: Material, data: Byte, title: String): Button {
            return object : Button() {
                override fun getName(player: Player): String {
                    return title
                }

                override fun getDescription(player: Player): List<String>? {
                    return listOf()
                }

                override fun getMaterial(player: Player): Material {
                    return material
                }

                override fun getDamageValue(player: Player): Byte {
                    return data
                }
            }
        }

        @JvmStatic fun fromItem(item: ItemStack?): Button {
            return object : Button() {
                override fun getButtonItem(player: Player): ItemStack {
                    return item ?: ItemStack(Material.AIR)
                }

                override fun getName(player: Player): String? {
                    return null
                }

                override fun getDescription(player: Player): List<String>? {
                    return null
                }

                override fun getMaterial(player: Player): Material? {
                    return null
                }
            }
        }

        @JvmStatic internal fun playFail(player: Player) {
            SoundCompat.FAILED_CLICK.playSound(player)
        }

        @JvmStatic internal fun playSuccess(player: Player) {
            SoundCompat.SUCCESSFUL_CLICK.playSound(player)
        }

        @JvmStatic internal fun playNeutral(player: Player) {
            SoundCompat.NEUTRAL_CLICK.playSound(player)
        }
    }

    abstract fun getName(player: Player): String?

    abstract fun getDescription(player: Player): List<String>?

    abstract fun getMaterial(player: Player): Material?

    open fun getDamageValue(player: Player): Byte {
        return 0
    }

    open fun applyMetadata(player: Player, itemMeta: ItemMeta): ItemMeta? {
        return null
    }

    open fun clicked(player: Player, slot: Int, clickType: ClickType) {}

    open fun shouldCancel(player: Player, slot: Int, clickType: ClickType): Boolean {
        return true
    }

    open fun getAmount(player: Player): Int {
        return 1
    }

    open fun getButtonItem(player: Player): ItemStack {
        val buttonItem = ItemStack(getMaterial(player)?: Material.AIR, getAmount(player), getDamageValue(player).toShort())
        val meta = buttonItem.itemMeta

        if (meta != null) {
            meta.displayName = ChatColor.translateAlternateColorCodes('&', getName(player) ?: " ")

            val description = getDescription(player)
            if (description != null) {
                meta.lore = description.stream().map { line -> ChatColor.translateAlternateColorCodes('&', line) }.collect(Collectors.toList())
            }

            val appliedMeta = applyMetadata(player, meta) ?: meta
            buttonItem.itemMeta = appliedMeta
        }

        return buttonItem
    }

}