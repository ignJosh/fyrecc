package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import redis.clients.jedis.Jedis

object ClearCacheCommand {
    @Command(["clearcache"], permission = "op")
    @JvmStatic
    fun execute(sender: CommandSender) {
        Stark.instance.core.redis.runBackboneRedisCommand{ redis ->
            redis.set("UUIDCache", null)
        }

        Stark.instance.core.uuidCache.reset()

        Stark.instance.server.onlinePlayers.forEach { player ->
            Stark.instance.core.uuidCache.cache(player.uniqueId, player.name)
        }

        sender.sendMessage("${ChatColor.GREEN}Cleared UUID cache...")
    }
}