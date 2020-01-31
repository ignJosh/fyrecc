package net.evilblock.stark.engine.command.data.parameter.impl

import net.evilblock.stark.engine.command.data.parameter.ParameterType
import net.evilblock.stark.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.inventory.ItemStack
import org.bukkit.entity.Player
import org.bukkit.command.CommandSender

class ItemStackParameterType : ParameterType<ItemStack?> {

    override fun transform(sender: CommandSender, source: String): ItemStack? {
        val item = ItemUtils[source]

        if (item == null) {
            sender.sendMessage("${ChatColor.RED}No item with the name $source found.")
            return null
        }

        return item
    }

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return listOf()
    }

}