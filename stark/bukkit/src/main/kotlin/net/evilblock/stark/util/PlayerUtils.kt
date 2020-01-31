package net.evilblock.stark.util

import net.evilblock.stark.engine.protocol.InventoryAdapter
import net.evilblock.stark.engine.protocol.PingAdapter
import org.bukkit.GameMode
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import java.lang.reflect.Field

object PlayerUtils {

    private val CURRENT_TICK_FIELD: Field

    init {
        val minecraftServerClass = Reflections.getNMSClass("MinecraftServer")!!
        CURRENT_TICK_FIELD = Reflections.getField(minecraftServerClass, "currentTick")!!
    }

    @JvmOverloads
    @JvmStatic
    fun resetInventory(player: Player, gameMode: GameMode? = null) {
        player.health = player.maxHealth
        player.fallDistance = 0.0f
        player.foodLevel = 20
        player.saturation = 10.0f
        player.level = 0
        player.exp = 0.0f

        if (!player.hasMetadata("modmode")) {
            player.inventory.clear()
            player.inventory.armorContents = null
        }

        player.fireTicks = 0

        for (potionEffect in player.activePotionEffects) {
            player.removePotionEffect(potionEffect.type)
        }

        if (gameMode != null && player.gameMode != gameMode) {
            player.gameMode = gameMode
        }
    }

    @JvmStatic
    fun getDamageSource(damager: Entity): Player? {
        var playerDamager: Player? = null
        if (damager is Player) {
            playerDamager = damager
        } else if (damager is Projectile) {
            val projectile = damager
            if (projectile.shooter is Player) {
                playerDamager = projectile.shooter as Player
            }
        }
        return playerDamager
    }

    @JvmStatic
    fun hasOpenInventory(player: Player): Boolean {
        return hasOwnInventoryOpen(player) || hasOtherInventoryOpen(player)
    }

    @JvmStatic
    fun hasOwnInventoryOpen(player: Player): Boolean {
        return InventoryAdapter.currentlyOpen.contains(player.uniqueId)
    }

    @JvmStatic
    fun hasOtherInventoryOpen(player: Player): Boolean {
        return Reflections.getFieldValue(Reflections.getFieldValue(Reflections.getHandle(player)!!, "activeContainer")!!, "windowId") as Int != 0
    }

    @JvmStatic
    fun getPing(player: Player): Int {
        return Reflections.getFieldValue(Reflections.getHandle(player)!!, "ping") as Int
    }

    @JvmStatic
    fun isLagging(player: Player): Boolean {
        return !PingAdapter.lastReply.containsKey(player.uniqueId) || (CURRENT_TICK_FIELD.get(null) as Int) - PingAdapter.lastReply[player.uniqueId]!! > 40
    }

}