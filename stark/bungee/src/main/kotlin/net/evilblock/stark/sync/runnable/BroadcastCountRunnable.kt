package net.evilblock.stark.sync.runnable

import net.evilblock.stark.Stark

class BroadcastCountRunnable : Runnable {

    override fun run() {
        Stark.instance.proxy.scheduler.runAsync(Stark.instance) {
            Stark.instance.syncHandler.broadcastGlobalOnlineCount()
        }
    }

}