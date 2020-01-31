package net.evilblock.stark.engine.protocol

import net.evilblock.stark.Stark
import net.evilblock.stark.util.Reflections
import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.lang.Exception
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PingAdapter : PacketAdapter(Stark.instance, PacketType.Play.Server.KEEP_ALIVE, PacketType.Play.Client.KEEP_ALIVE), Listener {

    override fun onPacketSending(event: PacketEvent) {
        val id = try {
            event.packet.integers.read(0) as Int
        } catch (e: Exception) {
            (event.packet.longs.read(0) as Long).toInt()
        }

        callbacks[event.player.uniqueId] = object : PingCallback(id) {
            override fun call() {
                val ping = (System.currentTimeMillis() - this.sendTime).toInt()
                Companion.ping[event.player.uniqueId] = ping
                lastReply[event.player.uniqueId] = Reflections.getFieldValue(Reflections.getMinecraftServer()!!, "currentTick") as Int
            }
        }
    }

    override fun onPacketReceiving(event: PacketEvent) {
        val iterator = callbacks.entries.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()

            val id = try {
                event.packet.integers.read(0) as Int
            } catch (e: Exception) {
                (event.packet.longs.read(0) as Long).toInt()
            }

            if (entry.value.id == id) {
                entry.value.call()
                iterator.remove()
                break
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        ping.remove(event.player.uniqueId)
        lastReply.remove(event.player.uniqueId)
        callbacks.remove(event.player.uniqueId)
    }

    private abstract class PingCallback
    constructor(val id: Int) {
        val sendTime: Long = System.currentTimeMillis()

        abstract fun call()
    }

    companion object {
        private val callbacks: ConcurrentHashMap<UUID, PingCallback> = ConcurrentHashMap()
        val ping: ConcurrentHashMap<UUID, Int> = ConcurrentHashMap()
        val lastReply: ConcurrentHashMap<UUID, Int> = ConcurrentHashMap()

        fun averagePing(): Int {
            if (ping.isEmpty()) {
                return 0
            }
            var x = 0
            for (p in ping.values) {
                x += p
            }
            return x / ping.size
        }
    }
}