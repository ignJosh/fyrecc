package net.evilblock.stark.core.grant

import java.util.*

open class Grant(val uuid: UUID = UUID.randomUUID()) {

    lateinit var reason: String
    var issuedBy: UUID? = null
    var issuedAt: Long = -1
    var expiresAt: Long? = null
    var removalReason: String?= null
    var removedBy: UUID?= null
    var removedAt: Long?= null

}