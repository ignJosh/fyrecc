package net.evilblock.stark.event

import net.evilblock.stark.profile.ProxyProfile
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.plugin.Cancellable
import net.md_5.bungee.api.plugin.Event

class LoginProfileLoadedEvent(val profile: ProxyProfile) : Event(), Cancellable {

    private var cancelled: Boolean = false
    var cancelReasons: Array<BaseComponent>? = null

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancelled: Boolean) {
        this.cancelled = cancelled
    }

}
