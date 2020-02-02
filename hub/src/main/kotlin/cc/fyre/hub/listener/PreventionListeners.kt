package cc.fyre.hub.listener

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.event.player.*
import org.bukkit.event.weather.ThunderChangeEvent
import org.bukkit.event.weather.WeatherChangeEvent

class PreventionListeners : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null
    }

    @EventHandler
    fun onPlayerKick(event: PlayerKickEvent) {
        event.leaveMessage = null
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.quitMessage = null
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent) {
        if (event.entityType == EntityType.ARROW) {
            event.entity.remove()
        }
    }

    @EventHandler
    fun onEntityDamageEvent(event: EntityDamageEvent) {
        if (event.entity is Player) {
            event.isCancelled = true

            if (event.cause == EntityDamageEvent.DamageCause.VOID || event.entity.location.y < 0) {
                event.entity.teleport(event.entity.world.spawnLocation)
            }
        }
    }

    @EventHandler
    fun onFoodLevelChangeEvent(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerExpChangedEvent(event: PlayerExpChangeEvent) {
        event.player.exp = 0.0F
    }

    @EventHandler
    fun onThunderChange(event: ThunderChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onWeatherChange(event: WeatherChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onCreatureSpawn(event: CreatureSpawnEvent) {
        if (!(event.spawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG ||
                        event.spawnReason == CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage = null
        event.droppedExp = 0
    }

    @EventHandler
    fun onBlockSpread(event: BlockSpreadEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onLeavesDecay(event: LeavesDecayEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBlockFade(event: BlockFadeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBlockForm(event: BlockFormEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerPortal(event: PlayerPortalEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun onBlockBreakEvent(event: BlockBreakEvent) {
        if (!canPerformAction(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockPlaceEvent(event: BlockPlaceEvent) {
        if (!canPerformAction(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerBucketFillEvent(event: PlayerBucketFillEvent) {
        if (!canPerformAction(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerBucketEmptyEvent(event: PlayerBucketEmptyEvent) {
        if (!canPerformAction(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerDropItemEvent(event: PlayerDropItemEvent) {
        if (!canPerformAction(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerPickupItemEvent(event: PlayerPickupItemEvent) {
        if (!canPerformAction(event.player)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        if (!canPerformAction(event.whoClicked as Player)) {
            if (event.clickedInventory == event.whoClicked.inventory) {
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.PHYSICAL) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPrepareCraft(event: PrepareItemCraftEvent) {
        event.inventory.result = null
    }

    @EventHandler
    fun onCraft(event: CraftItemEvent) {
        event.isCancelled = true
    }

    private fun canPerformAction(player: Player): Boolean {
        if (!player.isOp || !player.hasMetadata("Build") || player.gameMode != GameMode.CREATIVE) {
            return false
        }

        return true
    }

}