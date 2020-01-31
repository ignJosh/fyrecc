package net.evilblock.stark.rankconverter.conversion

import net.evilblock.stark.Stark
import net.evilblock.stark.core.rank.Rank
import net.evilblock.stark.rankconverter.RankConverter
import me.lucko.luckperms.api.Contexts
import me.lucko.luckperms.api.Group
import me.lucko.luckperms.api.LuckPermsApi
import org.bukkit.Bukkit
import org.bukkit.ChatColor

object LuckPermsRankConverter : RankConverter {

    override fun convert() {
        val provider = Bukkit.getServicesManager().getRegistration(LuckPermsApi::class.java)

        if (provider != null) {
            log("LuckPerms provider found")

            val weightedGroups = provider.provider.groups.sortedByDescending { group -> group.weight.orElse(0) }
            log("Found ${weightedGroups.size} groups")  //but from what i can see it cant even find the ranks

            val rankToGroup = hashMapOf<Rank, Group>()

            // create ranks from groups
            for ((orderIndex, group) in weightedGroups.withIndex()) {
                val rank = Rank(group.name)
                rank.displayName = group.displayName ?: group.name
                rank.displayOrder = orderIndex
                rank.prefix = group.cachedData.getMetaData(Contexts.allowAll()).prefix ?: ""
                rank.playerListPrefix = ChatColor.getLastColors(rank.prefix)
                rank.gameColor = ChatColor.getLastColors(rank.prefix)
                rank.permissions = group.ownNodes.map { node -> node.permission }.toMutableList()

                rankToGroup[rank] = group
            }

            // setup rank inheritance
            for ((rank, group) in rankToGroup) {
                val inherits = arrayListOf<String>()

                for ((otherRank, otherGroup) in rankToGroup) {
                    if (rank != otherRank) {
                        if (group.inheritsGroup(otherGroup)) {
                            inherits.add(otherRank.id)
                        }
                    }
                }

                rank.inherits = inherits
            }

            // save newly created ranks
            for (rank in rankToGroup.keys) {
                Stark.instance.core.rankHandler.getMutableRankMap()[rank.id] = rank

                log(rank.displayName) // if it prints in the console then it should saved ir read at least :)
            }

            Stark.instance.core.rankHandler.saveAllRanks()

            log("Saved ${rankToGroup.size} ranks")

            // apply ranks to users from their groups
            provider.provider.userManager.uniqueUsers.thenAcceptAsync { userSet ->
                userSet.forEach { uuid ->
                    provider.provider.userManager.loadUser(uuid).thenAcceptAsync { user ->
                        val rank = Stark.instance.core.rankHandler.getById(user.primaryGroup)

                        // check if a rank by an id of the user's primary group exists
                        if (rank != null) {
                            // populate uuid cache if name is not null, otherwise we'll lookup on cmd execute
                            if (user.name != null) {
                                Stark.instance.core.uuidCache.update(user.uuid, user.name!!)
                            }

                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ogrant ${user.uuid} ${user.primaryGroup} perm Rank Conversion")
                        }
                    }
                }
            }
        }
    }

}