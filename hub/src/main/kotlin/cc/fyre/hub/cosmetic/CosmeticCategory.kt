package cc.fyre.hub.cosmetic

import org.bukkit.entity.Player
import org.bukkit.material.MaterialData

interface CosmeticCategory {

    fun getIcon(): MaterialData

    fun getName(): String

    fun getCosmetics(): List<Cosmetic>

    fun getAccessableCosmetics(player: Player): List<Cosmetic> {
        return getCosmetics()
                .filter { cosmetic -> player.hasPermission(cosmetic.getPermission()) }
                .toList()
    }

}