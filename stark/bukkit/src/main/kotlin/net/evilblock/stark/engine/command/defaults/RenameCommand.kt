package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object RenameCommand {
    private val customNameStarter: String = ChatColor.translateAlternateColorCodes('&', "&b&c&f")

    @Command(["rename"], permission = "essentials.rename", description = "Rename the item you're currently holding. Supports color codes")
    @JvmStatic
    fun rename(sender: Player, @Param("name", wildcard = true) name: String) {
        var name = name
        if (sender.hasPermission("essentials.rename.color")) {
            name = ChatColor.translateAlternateColorCodes('&', name)
        }

        val item = sender.itemInHand
        if (item == null) {
            sender.sendMessage("${ChatColor.RED}You must be holding an item.")
            return
        }

        val isCustomEnchant = item.hasItemMeta() && item.itemMeta.hasDisplayName() && item.itemMeta.displayName.startsWith(customNameStarter)
        val meta = item.itemMeta
        meta.displayName = if (isCustomEnchant && !name.startsWith(customNameStarter)) customNameStarter + name else name
        item.itemMeta = meta

        sender.updateInventory()
        sender.sendMessage("${ChatColor.GOLD}Renamed your " + ChatColor.WHITE + ItemUtils.getName(ItemStack(item.type, item.amount, item.durability)) + ChatColor.GOLD + " to " + ChatColor.WHITE + name + ChatColor.GOLD + ".")
    }
}