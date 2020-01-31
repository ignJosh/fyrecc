package net.evilblock.stark.server.chat

import java.util.*
import kotlin.collections.HashMap

class SlowedChatSession(val issuer: String, val duration: Long) {
    val players = HashMap<UUID, Long>()
}