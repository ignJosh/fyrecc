package net.evilblock.stark.motd

data class MotdState(val name: String, val motd: String) {

    var countdown: Long? = null
    var countdownFinishMotd: String? = null

}