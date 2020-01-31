package net.evilblock.stark.core.rank

import net.evilblock.stark.core.StarkCore

class Rank(val id: String,
           var displayName: String,
           var displayOrder: Int,
           var permissions: MutableList<String>,
           var prefix: String,
           var playerListPrefix: String,
           var gameColor: String,
           var default: Boolean,
           var inherits: List<String>,
           var hidden: Boolean) {

    constructor(id: String) : this(id, id, 999, arrayListOf(), "", "", "&f", false, arrayListOf(), false)

    /**
     * Recursively gets all permissions of this rank and the ranks it inherits.
     */
    fun getCompoundedPermissions(): List<String> {
        val toReturn = ArrayList<String>()
        toReturn.addAll(permissions)

        for (inheritedRank in getInheritedRanks()) {
            toReturn.addAll(inheritedRank.getCompoundedPermissions())
        }

        return toReturn
    }

    fun getInheritedRanks(): List<Rank> {
        val toReturn = arrayListOf<Rank>()
        for (rank in StarkCore.instance.rankHandler.getRanks()) {
            if (inherits.contains(rank.id)) {
                toReturn.add(rank)
            }
        }
        return toReturn
    }

    fun getColoredName(): String {
        return gameColor.replace("&", "§") + displayName
    }

}