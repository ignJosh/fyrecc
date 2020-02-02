package cc.fyre.hub.cosmetic

import cc.fyre.hub.Hub

/**
 * Every cosmetic should calculate their tick executions
 * based on the tick interval of this runnable.
 */
class CosmeticsTickRunnable : Runnable {

    override fun run() {
        Hub.instance.server.onlinePlayers.forEach { player ->
            Hub.instance.cosmetics.categories.forEach { category ->
                category.getCosmetics().forEach { cosmetic ->
                    if (Hub.instance.cosmetics.isCosmeticEnabled(player, cosmetic)) {
                        cosmetic.onTick(player)
                    }
                }
            }
        }
    }

}