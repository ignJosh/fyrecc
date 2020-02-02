package cc.fyre.hub.cosmetic

import cc.fyre.hub.Hub
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class CosmeticListeners : Listener {

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        Hub.instance.server.scheduler.runTaskAsynchronously(Hub.instance) {
            val profile = Hub.instance.cosmetics.loadProfile(event.player.uniqueId)

            Hub.instance.cosmetics.categories.forEach { category ->
                category.getAccessableCosmetics(event.player).forEach { cosmetic ->
                    if (profile.isCosmeticEnabled(cosmetic) || !cosmetic.canBeToggled()) {
                        cosmetic.onEnable(event.player)
                    }
                }
            }
        }
    }

}