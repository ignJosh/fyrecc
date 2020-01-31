package net.evilblock.stark.profile

import net.evilblock.stark.Stark
import net.evilblock.stark.core.profile.Profile
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.permissions.PermissionAttachment
import java.util.*

class BukkitProfile(uuid: UUID): Profile(uuid) {

    var attachment: PermissionAttachment? = null

    fun getPlayer(): Player? {
        return Bukkit.getPlayer(this.uuid)
    }

    override fun apply() {
        val player = this.getPlayer() ?: return

        if (attachment == null) {
            attachment = player.addAttachment(Stark.instance)
        } else {
            val permissions = listOf<String>(*attachment!!.permissions.keys.toTypedArray())

            for (permission in permissions) {
                attachment!!.unsetPermission(permission)
            }
        }

        for (permission in getCompoundedPermissions()) {
            if (permission.startsWith("-")) {
                attachment!!.setPermission(permission.substring(1), false)
            } else {
                attachment!!.setPermission(permission, true)
            }
        }

        var playerListName = getPlayerListName()

        if (playerListName.length > 16) {
            playerListName = playerListName.substring(0, 15)
        }

        player.displayName = ChatColor.translateAlternateColorCodes('&', getRank().gameColor) + Stark.instance.core.uuidCache.name(uuid)
//        player.playerListName = playerListName
        player.setMetadata("RankPrefix", FixedMetadataValue(Stark.instance, ChatColor.translateAlternateColorCodes('&', getRank().prefix)))
    }

}