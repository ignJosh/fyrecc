package net.evilblock.stark.hook

import net.evilblock.stark.Stark
import net.milkbowl.vault.permission.Permission
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority

object VaultHook {

    fun hook() {
        if (Bukkit.getServer().pluginManager.getPlugin("Vault") == null) {
            return
        }

        Bukkit.getServer().servicesManager.register(Permission::class.java, VaultPermissionImpl(), Stark.instance, ServicePriority.Highest)
    }

}