package cc.fyre.hub.cosmetic

import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

interface Cosmetic : Listener {

    fun getIcon(): ItemStack

    fun getName(): String

    fun getDescription(): List<String>

    fun getPermission(): String

    fun hiddenIfNotPermitted(): Boolean {
        return false
    }

    fun canBeToggled(): Boolean {
        return true
    }

    fun onEnable(player: Player) {}

    fun onDisable(player: Player) {}

    fun onTick(player: Player) {}

    fun getSerializedName(): String {
        return getName().replace(" ", "_")
    }

}