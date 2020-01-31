package net.evilblock.stark.core.uuid

import net.evilblock.stark.core.StarkCore

class UUIDCacheLoadRunnable : Runnable {

    override fun run() {
        StarkCore.instance.uuidCache.load()
    }

}