package net.evilblock.stark.rankconverter

import net.evilblock.stark.Stark

interface RankConverter {

    fun convert()

    fun log(message: String) {
        Stark.instance.logger.info("[Rank Conversion] $message")
    }

}