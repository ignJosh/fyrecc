package net.evilblock.stark.core.profile

import net.evilblock.stark.core.StarkCore
import net.evilblock.stark.core.profile.grant.ProfileGrant
import net.evilblock.stark.core.profile.punishment.ProfilePunishment
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentType
import net.evilblock.stark.core.profile.grant.ProfileGrantSerializer
import net.evilblock.stark.core.profile.lookup.GeoInfo
import net.evilblock.stark.core.profile.punishment.ProfilePunishmentSerializer
import net.evilblock.stark.core.rank.Rank
import org.bson.Document
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

abstract class Profile(val uuid: UUID) {

    var rankGrants: MutableList<ProfileGrant> = ArrayList()
    var punishments: MutableList<ProfilePunishment> = ArrayList()
    var dateJoined = Date()
    var ipAddresses: MutableSet<String> = HashSet()
    var geoInfo: GeoInfo? = null

    fun getCompoundedPermissions(): List<String> {
        val permissions = ArrayList<String>()
        permissions.addAll(StarkCore.instance.rankHandler.getDefaultRank().getCompoundedPermissions())

        ArrayList(this.rankGrants)
                .sortedBy { grant -> grant.rank.displayOrder }
                .forEach { grant ->
                    if (grant.isActive()) {
                        permissions.addAll(grant.rank.getCompoundedPermissions())
                    }
                }

        return permissions
    }

    fun getRank(): Rank {
        var currentGrant: ProfileGrant? = null

        for (grant in this.rankGrants) {
            if (grant.isActive()) {
                if (currentGrant == null) {
                    currentGrant = grant
                    continue
                }

                if (currentGrant.rank.displayOrder > grant.rank.displayOrder) {
                    currentGrant = grant
                }
            }
        }

        return currentGrant?.rank ?: StarkCore.instance.rankHandler.getDefaultRank()
    }

    fun getPlayerListName(): String {
        return getRank().playerListPrefix.replace('&', StarkCore.COLOR_CODE_CHAR) + StarkCore.instance.uuidCache.name(this.uuid)
    }

    fun getDisplayName(): String {
        return getRank().prefix.replace('&', StarkCore.COLOR_CODE_CHAR) + StarkCore.instance.uuidCache.name(this.uuid)
    }

    fun update(document: Document) {
        val freshGrants = ArrayList<ProfileGrant>()
        val freshPunishments = ArrayList<ProfilePunishment>()

        for (grantDocument in document.getList("grants", Document::class.java)) {
            try {
                freshGrants.add(ProfileGrantSerializer.deserialize(grantDocument))
            } catch (ignore: IllegalStateException) { }
        }

        for (punishmentDocument in document.getList("punishments", Document::class.java)) {
            try {
                freshPunishments.add(ProfilePunishmentSerializer.deserialize(punishmentDocument))
            } catch (ignore: IllegalStateException) { }
        }

        this.rankGrants = freshGrants
        this.punishments = freshPunishments
        this.dateJoined = Date(document.getLong("dateJoined")!!)
        this.ipAddresses = document.getList("ipAddresses", String::class.java).toHashSet()

        if (document["geoInfo"] != null) {
            val geoInfoDocument = document["geoInfo"] as Document

            this.geoInfo = GeoInfo(
                    geoInfoDocument.getString("ip"),
                    geoInfoDocument.getString("city"),
                    geoInfoDocument.getString("region"),
                    geoInfoDocument.getString("country"),
                    geoInfoDocument.getString("postal")
            )
        }
    }

    fun getActivePunishment(type: ProfilePunishmentType): ProfilePunishment? {
        for (punishment in this.punishments) {
            if (punishment.type == type && punishment.isActive()) {
                return punishment
            }
        }
        return null
    }

    fun hasPermission(permission: String): Boolean {
        return getCompoundedPermissions().contains(permission)
    }

    abstract fun apply()

}