package net.evilblock.stark.hook

import net.evilblock.stark.Stark
import net.evilblock.stark.core.profile.grant.ProfileGrant
import net.milkbowl.vault.permission.Permission
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Player

class VaultPermissionImpl : Permission() {

    override fun hasSuperPermsCompat(): Boolean {
        return true
    }

    override fun getPlayerGroups(player: Player): Array<String> {
        val groups = arrayListOf<String>()
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)

        profile?.rankGrants?.filter { grant -> grant.isActive() }?.forEach {
            groups.add(it.rank.id)
        }

        return groups.toTypedArray()
    }

    // TODO: ASYNC OFFLINE SUPPORT
    override fun getPlayerGroups(world: String, player: OfflinePlayer): Array<String> {
        return emptyArray()
    }

    override fun getPrimaryGroup(player: Player): String {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)
        return profile?.getRank()?.id ?: Stark.instance.core.rankHandler.getDefaultRank().id
    }

    // TODO: ASYNC OFFLINE SUPPORT
    override fun getPrimaryGroup(world: String, player: OfflinePlayer): String {
        return Stark.instance.core.rankHandler.getDefaultRank().id
    }

    override fun groupAdd(world: World, group: String, permission: String): Boolean {
        val rank = Stark.instance.core.rankHandler.getById(group)

        return if (rank != null) {
            val success = rank.permissions.add(permission)

            if (success) {
                Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                    Stark.instance.core.rankHandler.saveRank(rank)
                }
            }

            return success
        } else {
            false
        }
    }

    override fun groupHas(world: World, group: String, permission: String): Boolean {
        val rank = Stark.instance.core.rankHandler.getById(group)
        return rank?.permissions?.contains(permission) ?: false
    }

    override fun groupRemove(world: World, group: String, permission: String): Boolean {
        val rank = Stark.instance.core.rankHandler.getById(group)

        return if (rank != null) {
            val success = rank.permissions.remove(permission)

            if (success) {
                Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                    Stark.instance.core.rankHandler.saveRank(rank)
                }
            }

            return success
        } else {
            false
        }
    }

    override fun playerAddGroup(player: Player, group: String): Boolean {
        val rank = Stark.instance.core.rankHandler.getById(group) ?: return false

        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)

        if (profile != null) {
            profile.rankGrants.filter { it.isActive() }.forEach { grant ->
                if (grant.rank.id.equals(group, ignoreCase = true)) {
                    return@forEach
                }
            }

            // TODO: MAKE VAULT GRANT'S DEFAULT DURATION CONFIGURABLE
            val grant = ProfileGrant()
            grant.rank = rank
            grant.reason = "VaultAPI Hook"
            grant.issuedAt = System.currentTimeMillis()

            profile.rankGrants.add(grant)

            Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                Stark.instance.core.getProfileHandler().saveProfile(profile)
            }

            return true
        }

        return false
    }

    // TODO: ASYNC OFFLINE SUPPORT
    override fun playerAddGroup(world: String, player: OfflinePlayer, group: String): Boolean {
        return false
    }

    override fun playerInGroup(player: Player, group: String): Boolean {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)

        profile?.rankGrants?.filter { it.isActive() }?.forEach { grant ->
            if (grant.rank.id.equals(group, ignoreCase = true)) {
                return true
            }
        }

        return false
    }

    // TODO: ASYNC OFFLINE SUPPORT
    override fun playerInGroup(world: String, player: OfflinePlayer, group: String): Boolean {
        return super.playerInGroup(world, player, group)
    }

    override fun playerRemoveGroup(player: Player, group: String): Boolean {
        val profile = Stark.instance.core.getProfileHandler().getByUUID(player.uniqueId)

        if (profile != null) {
            profile.rankGrants.filter { it.isActive() }.forEach { grant ->
                if (grant.rank.id.equals(group, ignoreCase = true)) {
                    grant.removedAt = System.currentTimeMillis()
                    grant.removalReason = "VaultAPI Hook"
                    return true
                }
            }

            Stark.instance.server.scheduler.runTaskAsynchronously(Stark.instance) {
                Stark.instance.core.getProfileHandler().saveProfile(profile)
            }
        }

        return false
    }

    // TODO: ASYNC OFFLINE SUPPORT
    override fun playerRemoveGroup(world: String, player: OfflinePlayer, group: String): Boolean {
        return super.playerRemoveGroup(world, player, group)
    }

    override fun playerHas(world: String, player: String, permission: String): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun playerAdd(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun playerRemove(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun getPlayerGroups(p0: String?, p1: String?): Array<String> {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun getPrimaryGroup(p0: String?, p1: String?): String {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun playerAddGroup(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun playerInGroup(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun playerRemoveGroup(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun getGroups(): Array<String> {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun getName(): String {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun groupAdd(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun groupHas(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun groupRemove(p0: String?, p1: String?, p2: String?): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun hasGroupSupport(): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

    override fun isEnabled(): Boolean {
        throw UnsupportedOperationException("Stark does not support deprecated VaultAPI methods")
    }

}