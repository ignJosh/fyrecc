package net.evilblock.stark.server

import net.evilblock.stark.Stark
import net.evilblock.stark.server.chat.ChatFilterEntry
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.TimeUnit
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import java.util.HashSet

class ServerHandler {

    var frozen: Boolean = false
    val disallowedCommands = HashSet<String>()

    val bannedRegexes = setOf(
            ChatFilterEntry("Restricted Phrase \"ip farm\"", "[i1l1|]+p+ ?f[a4]+rm+"),
            ChatFilterEntry("Racism \"Nigger\"", "n+[i1l|]+gg+[e3]+r+"),
            ChatFilterEntry("Racism \"Beaner\"", "b+e+a+n+e+r+"),
            ChatFilterEntry("Suicide Encouragement", "k+i+l+l+ *y*o*u+r+ *s+e+l+f+"),
            ChatFilterEntry("Suicide Encouragement", "\\bk+y+s+\\b"),
            ChatFilterEntry("Offensive \"Faggot\"", "f+a+g+[o0]+t+"),
            ChatFilterEntry("IP Address", "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])([.,])){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"),
            ChatFilterEntry("Phishing Link \"optifine\"", "optifine\\.(?=\\w+)(?!net)"),
            ChatFilterEntry("Phishing Link \"gyazo\"", "gyazo\\.(?=\\w+)(?!com)"),
            ChatFilterEntry("Phishing Link \"prntscr\"", "prntscr\\.(?=\\w+)(?!com)")
    )

    fun load() {
        val list = Stark.instance.config.getStringList("disallowedCommands")

        if (list != null) {
            disallowedCommands.addAll(list)
        }

        disallowedCommands.add("/calc")
        disallowedCommands.add("/calculate")
        disallowedCommands.add("/eval")
        disallowedCommands.add("/evaluate")
        disallowedCommands.add("/solve")
        disallowedCommands.add("/worldedit:calc")
        disallowedCommands.add("/worldedit:eval")
        disallowedCommands.add("me")
        disallowedCommands.add("pl")
        disallowedCommands.add("bukkit:me")
        disallowedCommands.add("icanhasbukkit")
        disallowedCommands.add("bukkit:icanhasbukkit")
        disallowedCommands.add("minecraft:me")
        disallowedCommands.add("bukkit:plugins")
        disallowedCommands.add("bukkit:pl")
    }

    fun freeze(player: Player) {
        player.setMetadata("frozen", FixedMetadataValue(Stark.instance, true as Any) as MetadataValue)
        player.sendMessage("${ChatColor.RED}You have been frozen by a staff member.")

        val uuid = player.uniqueId

        Bukkit.getScheduler().runTaskLater(Stark.instance, { this.unfreeze(uuid) }, 20L * TimeUnit.HOURS.toSeconds(2L))

        val location = player.location
        var tries = 0

        while (1.0 <= location.y && !location.block.type.isSolid && tries++ < 100) {
            location.subtract(0.0, 1.0, 0.0)
            if (location.y <= 0.0) {
                break
            }
        }

        if (100 <= tries) {
            Bukkit.getLogger().info("Hit the 100 try limit on the freeze command.")
        }

        location.y = location.blockY.toDouble()

        player.teleport(location.add(0.0, 1.0, 0.0))
    }

    fun unfreeze(uuid: UUID) {
        val player = Bukkit.getPlayer(uuid)
        if (player != null) {
            player.removeMetadata("frozen", Stark.instance)
            player.sendMessage("${ChatColor.GREEN}You have been unfrozen by a staff member.")
        }
    }

}