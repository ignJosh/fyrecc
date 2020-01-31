package net.evilblock.stark.rankconverter

import net.evilblock.stark.Stark
import net.evilblock.stark.rankconverter.command.LuckPermsConvertCommand
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class StarkRankConverter : JavaPlugin() {

    override fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            logger.info("Detected LuckPerms plugin!")

            Stark.instance.commandHandler.registerClass(LuckPermsConvertCommand::class.java)
        }
    }
}