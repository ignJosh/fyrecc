package net.evilblock.stark.util

import net.evilblock.stark.Stark
import com.google.common.io.ByteStreams
import org.bukkit.entity.Player

object BungeeUtil {

    fun sendToServer(player: Player, server: String) {
        try {
            val out = ByteStreams.newDataOutput()
            out.writeUTF("Connect")
            out.writeUTF(server)

            player.sendPluginMessage(Stark.instance, "BungeeCord", out.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}