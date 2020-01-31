package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object HeadCommand {
    @Command(["head"], permission = "essentials.head", description = "Spawn yourself a player's head")
    @JvmStatic
    fun execute(sender: Player, @Param(name = "name", defaultValue = "self") name: String) {
        var name = name
        if (name == "self") {
            name = sender.name
        }
        val item = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())
        val meta = item.itemMeta as SkullMeta
        meta.owner = name
        item.itemMeta = meta
        sender.inventory.addItem(item)
        sender.sendMessage(ChatColor.GOLD.toString() + "You were given " + ChatColor.WHITE + name + ChatColor.GOLD + "'s head.")
    }
}