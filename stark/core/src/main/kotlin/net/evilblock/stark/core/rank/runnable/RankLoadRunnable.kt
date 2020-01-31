package net.evilblock.stark.core.rank.runnable

import net.evilblock.stark.core.StarkCore

class RankLoadRunnable : Runnable {

    override fun run() {
        StarkCore.instance.rankHandler.loadRanks()
    }

}