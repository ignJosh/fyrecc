package net.evilblock.stark.core.grant

import org.bson.Document
import java.util.*

object GrantSerializer {

    @JvmStatic
    fun serialize(document: Document, grant: Grant): Document {
        document["id"] = grant.uuid.toString()
        document["reason"] = grant.reason
        document["issuedBy"] = if (grant.issuedBy != null) grant.issuedBy.toString() else null
        document["issuedAt"] = grant.issuedAt
        document["expiresAt"] = grant.expiresAt
        document["removalReason"] = grant.removalReason
        document["removedBy"] = if (grant.removedBy != null) grant.removedBy!!.toString() else null
        document["removedAt"] = grant.removedAt
        return document
    }

    @JvmStatic
    fun <T : Grant> deserialize(document: Document, grant: T): T {
        grant.reason = document.getString("reason")
        grant.issuedAt = document.getLong("issuedAt")!!

        val addedBy = document.getString("issuedBy")
        if (addedBy != null) {
            grant.issuedBy = UUID.fromString(addedBy)
        }

        grant.expiresAt = document.getLong("expiresAt")
        grant.removalReason = document.getString("removalReason")
        grant.removedAt = document.getLong("removedAt")

        val removedBy = document.getString("removedBy")
        if (removedBy != null) {
            grant.removedBy = UUID.fromString(removedBy)
        }

        return grant
    }

}