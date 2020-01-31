package net.evilblock.stark.core.profile.grant

import net.evilblock.stark.core.StarkCore
import net.evilblock.stark.core.grant.GrantSerializer
import org.bson.Document
import java.util.*

object ProfileGrantSerializer {

    @JvmStatic
    fun serialize(grant: ProfileGrant): Document {
        val document = GrantSerializer.serialize(Document(), grant)
        document["rank"] = grant.rank.id
        return document
    }

    @JvmStatic
    fun deserialize(document: Document): ProfileGrant {
        val rank = StarkCore.instance.rankHandler.getById(document.getString("rank"))
                ?: throw IllegalStateException("Rank doesn't exist")

        val grant = GrantSerializer.deserialize(document, ProfileGrant(UUID.fromString(document.getString("id"))))
        grant.rank = rank
        return grant
    }

}