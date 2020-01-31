package net.evilblock.stark.profile.grant.event

import net.evilblock.stark.core.profile.grant.ProfileGrant
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class GrantCreatedEvent(val player: Player, val grant: ProfileGrant) : Event() {

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

}