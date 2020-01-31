package net.evilblock.stark.profile

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ProfileCommands {

    @Command(names = ["geoinfo"], permission = "op", async = true)
    @JvmStatic
    fun execute(player: Player, @Param(name = "player") target: BukkitProfile) {
        if (target.geoInfo == null) {
            player.sendMessage("${ChatColor.RED}No GeoInfo stored for ${target.getPlayerListName()}${ChatColor.RED}.")
        } else {
            player.sendMessage("")
            player.sendMessage("${ChatColor.GOLD}Geo Information of ${Stark.instance.core.uuidCache.name(target.uuid)}")
            player.sendMessage("${ChatColor.GOLD}City: ${ChatColor.WHITE}${target.geoInfo!!.city}")
            player.sendMessage("${ChatColor.GOLD}Region: ${ChatColor.WHITE}${target.geoInfo!!.region}")
            player.sendMessage("${ChatColor.GOLD}Country: ${ChatColor.WHITE}${target.geoInfo!!.country}")
            player.sendMessage("${ChatColor.GOLD}Postal: ${ChatColor.WHITE}${target.geoInfo!!.postal}")
            player.sendMessage("")
        }
    }

}