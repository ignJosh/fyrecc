package net.evilblock.stark.core.profile.punishment

import net.evilblock.stark.core.grant.GrantSerializer
import org.bson.Document
import java.util.*

object ProfilePunishmentSerializer {

    @JvmStatic
    fun serialize(punishment: ProfilePunishment): Document {
        val document = GrantSerializer.serialize(Document(), punishment)
        document["type"] = punishment.type.name
        return document
    }

    @JvmStatic
    fun deserialize(document: Document): ProfilePunishment {
        val punishment = ProfilePunishment(UUID.fromString(document.getString("id")))
        punishment.type = ProfilePunishmentType.valueOf(document.getString("type"))
        return punishment
    }

}