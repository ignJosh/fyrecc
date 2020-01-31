package net.evilblock.stark.core.whitelist

enum class WhitelistType(val displayName: String, val disallowMessage: String) {
    NONE("None", ""),
    PURCHASED("Purchased", "&4The network is currently whitelisted.\n&4Additional info may be found at 1v1.club"),
    MAINTENANCE("Maintenance", "&4The network is currently in maintenance mode.\n46Visit our website or twitter for more information.");

    fun isAboveOrEqual(type: WhitelistType): Boolean {
        return this.ordinal >= type.ordinal
    }

    fun getPermission(): String {
        return "stark.whitelist.access.${this.name}"
    }
}