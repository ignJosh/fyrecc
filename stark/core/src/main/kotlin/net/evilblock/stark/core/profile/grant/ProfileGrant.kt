package net.evilblock.stark.core.profile.grant

import net.evilblock.stark.core.grant.Grant
import net.evilblock.stark.core.rank.Rank
import java.util.*

class ProfileGrant(uuid: UUID = UUID.randomUUID()) : Grant(uuid) {

    lateinit var rank: Rank

    fun isActive(): Boolean {
        return removedAt == null && (expiresAt == null || System.currentTimeMillis() < expiresAt!!)
    }

}