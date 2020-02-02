package cc.fyre.hub.cosmetic.categories.hidden.emote

import org.bukkit.Location
import org.bukkit.material.MaterialData

interface Emote {

    fun getDisplayName(): String

    fun getDescription(): List<String>

    fun getIcon(): MaterialData

    fun playEffect(location: Location)

}