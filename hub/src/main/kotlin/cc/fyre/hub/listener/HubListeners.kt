package cc.fyre.hub.listener

import cc.fyre.hub.Hub
import cc.fyre.hub.HubItems
import cc.fyre.hub.cosmetic.menu.EmoteBoxMenu
import cc.fyre.hub.menu.ServerSelectorMenu
import mkremins.fanciful.FancyMessage
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.EnderPearl
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileLaunchEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.spigotmc.event.entity.EntityDismountEvent

class HubListeners : Listener {

    companion object {
        private val PREFIX = FancyMessage("* ").color(ChatColor.GRAY).style(ChatColor.BOLD)
    }

    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        val player = event.player

        PREFIX.clone()
                .then("Welcome to the ").color(ChatColor.YELLOW).style(ChatColor.BOLD)
                .then("Kihar Network").color(ChatColor.GOLD).style(ChatColor.BOLD).then("!").color(ChatColor.YELLOW).style(ChatColor.BOLD)
                .send(player)

        PREFIX.clone()
                .then("Website: ").color(ChatColor.YELLOW).style(ChatColor.BOLD)
                .then("https://www.kihar.net").color(ChatColor.LIGHT_PURPLE).style(ChatColor.BOLD).link("https://www.kihar.net")
                .send(player)

        PREFIX.clone()
                .then("Discord: ").color(ChatColor.YELLOW).style(ChatColor.BOLD)
                .then("https://discord.gg/4V7phmq").color(ChatColor.LIGHT_PURPLE).style(ChatColor.BOLD).link("https://discord.gg/4V7phmq")
                .send(player)

        PREFIX.clone()
                .then("Teamspeak: ").color(ChatColor.YELLOW).style(ChatColor.BOLD)
                .then("ts.kihar.net").color(ChatColor.LIGHT_PURPLE).style(ChatColor.BOLD).link("ts.kihar.net")
                .send(player)

        PREFIX.clone()
                .then("Twitter: ").color(ChatColor.YELLOW).style(ChatColor.BOLD)
                .then("@KiharMC").color(ChatColor.LIGHT_PURPLE).style(ChatColor.BOLD).link("https://www.twitter.com/@KiharMC")
                .send(player)

        player.inventory.armorContents = arrayOfNulls(4)
        player.inventory.contents = arrayOfNulls(36)
        player.inventory.heldItemSlot = 0
        player.health = 20.0
        player.foodLevel = 20
        player.exp = 0.0F

        player.inventory.setItem(0, HubItems.SELECTOR)
        player.inventory.setItem(4, HubItems.COSMETICS)
        player.inventory.setItem(8, HubItems.ENDER_BUTT)

        player.updateInventory()

        player.teleport(Hub.instance.server.worlds[0].spawnLocation)
    }

    @EventHandler
    fun onPlayerInteractEvent(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            val itemInHand = event.player.itemInHand
            if (itemInHand != null) {
                when (itemInHand) {
                    HubItems.SELECTOR -> {
                        ServerSelectorMenu().openMenu(event.player)
                    }
                    HubItems.COSMETICS -> {
                        Hub.instance.cosmetics.openMenu(event.player)
                    }
                    HubItems.ENDER_BUTT -> {
                        if (event.action == Action.RIGHT_CLICK_BLOCK) {
                            event.isCancelled = true
                        }
                    }
                    HubItems.EMOTE_BOX -> {
                        EmoteBoxMenu().openMenu(event.player)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onProjectileLaunchEvent(event: ProjectileLaunchEvent) {
        if (event.entity is EnderPearl && event.entity.shooter is Player) {
            val player = event.entity.shooter as Player

            if (player.vehicle != null) {
                val vehicle = player.vehicle
                player.eject()
                vehicle.remove()
            }

            event.entity.velocity = player.location.direction.normalize().multiply(1.5F)
            event.entity.passenger = player

            player.world.playSound(player.location, Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F)

            // wait 1 tick before giving item back
            Hub.instance.server.scheduler.runTaskLater(Hub.instance, {
                player.inventory.setItem(8, HubItems.ENDER_BUTT)
                player.updateInventory()
            }, 1L)
        }
    }

    @EventHandler
    fun onEntityDismountEvent(event: EntityDismountEvent) {
        if (event.entity is Player && event.entity.vehicle is EnderPearl) {
            val vehicle = event.entity.vehicle

            event.entity.eject()

            // wait 1 tick before removing entity
            Hub.instance.server.scheduler.runTaskLater(Hub.instance, {
                if (!vehicle.isDead) {
                    vehicle.remove()
                }
            }, 1L)
        }
    }

}