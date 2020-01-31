package net.evilblock.stark.core.profile.punishment

import net.evilblock.stark.core.grant.Grant
import java.util.*

class ProfilePunishment(uuid: UUID = UUID.randomUUID()) : Grant(uuid) {

    lateinit var type: ProfilePunishmentType

    fun isActive(): Boolean {
        return removedAt == null && (expiresAt == null || System.currentTimeMillis() < expiresAt!!)
    }

}