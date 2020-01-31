package net.evilblock.stark.util.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class HourEvent constructor(val hour: Int) : Event() {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList: HandlerList = HandlerList()
    }

}