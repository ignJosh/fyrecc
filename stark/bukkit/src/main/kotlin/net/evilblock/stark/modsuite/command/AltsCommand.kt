package net.evilblock.stark.modsuite.command

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.profile.BukkitProfile
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object AltsCommand {

    @Command(["alts"], permission = "stark.alts", description = "Find a player's alts", async = true)
    @JvmStatic
    fun execute(sender: CommandSender, @Param("player") target: BukkitProfile) {
        val alts = Stark.instance.core.getProfileHandler().findAlts(target)

        sender.sendMessage("${ChatColor.RED}Found ${ChatColor.YELLOW}${alts.size} ${ChatColor.RED}alt${if (alts.size == 1) "" else "s"} for ${ChatColor.YELLOW}${target.getPlayerListName()}")

        if (alts.isNotEmpty()) {
            val names = alts.map { uuid ->
                val availability = Stark.instance.core.availabilityHandler.fetch(uuid)
                (if (availability.isOnline()) ChatColor.GREEN else ChatColor.GRAY).toString() + Stark.instance.core.uuidCache.name(uuid)
            }.toList()

            sender.sendMessage(names.joinToString("${ChatColor.GRAY}, "))
        }
    }

}