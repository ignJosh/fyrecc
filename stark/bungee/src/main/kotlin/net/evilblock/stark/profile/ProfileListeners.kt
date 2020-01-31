package net.evilblock.stark.profile

import net.evilblock.stark.Stark
import net.evilblock.stark.event.LoginProfileLoadedEvent
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class ProfileListeners : Listener {

    @EventHandler(priority = 99)
    fun onLoginEvent(event: LoginEvent) {
        if (!event.isCancelled) {
            val profile = Stark.instance.core.getProfileHandler().loadProfile(event.connection.uniqueId)
            Stark.instance.core.getProfileHandler().profiles[profile.uuid] = profile

            val profileEvent = LoginProfileLoadedEvent(profile)
            Stark.instance.proxy.pluginManager.callEvent(profileEvent)

            if (profileEvent.isCancelled) {
                event.isCancelled = true
                event.setCancelReason(*profileEvent.cancelReasons!!)
            }
        }
    }

    @EventHandler(priority = 0)
    fun onPostLoginEvent(event: PostLoginEvent) {
        Stark.instance.core.getProfileHandler().profiles[event.player.uniqueId]!!.apply()
    }

    @EventHandler(priority = 99)
    fun onPlayerDisconnectEvent(event: PlayerDisconnectEvent) {
        Stark.instance.core.getProfileHandler().profiles.remove(event.player.uniqueId)
    }

}