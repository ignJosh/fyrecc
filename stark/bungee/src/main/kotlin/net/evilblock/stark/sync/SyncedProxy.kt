package net.evilblock.stark.sync

data class SyncedProxy(val proxyId: String, var online: Int = 0, var lastUpdate: Long = System.currentTimeMillis()) {

    fun isOnline(): Boolean {
        return System.currentTimeMillis() - lastUpdate < 5000
    }

}