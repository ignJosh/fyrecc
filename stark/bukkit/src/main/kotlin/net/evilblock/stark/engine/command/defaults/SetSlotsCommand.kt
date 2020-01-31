package net.evilblock.stark.engine.command.defaults

import net.evilblock.stark.Stark
import net.evilblock.stark.engine.command.Command
import net.evilblock.stark.engine.command.data.parameter.Param
import net.evilblock.stark.util.Reflections
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.reflect.Field

object SetSlotsCommand {

    private var maxPlayerField: Field

    init {
        val playerListClass = Reflections.getNMSClass("PlayerList")
        maxPlayerField = playerListClass!!.getDeclaredField("maxPlayers")
    }

    init {
        maxPlayerField.isAccessible = true

        load()
    }

    @Command(["setslots"], permission = "essentials.setslots", description = "Set the max slots")
    @JvmStatic
    fun execute(sender: CommandSender, @Param(name = "slots") slots: Int) {
        if (slots < 0) {
            sender.sendMessage("${ChatColor.RED}The number of slots must be greater or equal to zero.")
            return
        }

        set(slots)
        sender.sendMessage("${ChatColor.GOLD}Slots set to ${ChatColor.WHITE}$slots${ChatColor.GOLD}.")
    }

    private fun set(slots: Int) {
        try {
            maxPlayerField.set(Reflections.callMethod(Bukkit.getServer()!!, "getHandle")!!, slots)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        save()
    }

    private fun save() {
        Stark.instance.config.set("Persistence.Slots", Bukkit.getMaxPlayers() as Any)
        Stark.instance.saveConfig()
    }

    private fun load() {
        if (Stark.instance.config.contains("Persistence.Slots")) {
            set(Stark.instance.config.getInt("Persistence.Slots"))
        } else {
            Stark.instance.config.set("Persistence.Slots", Bukkit.getMaxPlayers() as Any)
        }
    }

}