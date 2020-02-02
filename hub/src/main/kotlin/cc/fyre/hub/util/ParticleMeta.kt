package cc.fyre.hub.util

import org.bukkit.Effect
import org.bukkit.Location

data class ParticleMeta(val location: Location, val effect: Effect) {

    constructor(location: Location,
                effect: Effect,
                offsetX: Float,
                offsetY: Float,
                offsetZ: Float,
                speed: Float,
                amount: Int) : this(location, effect) {
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.offsetZ = offsetZ
        this.speed = speed
        this.amount = amount
    }

    var offsetX = 0.0F
    var offsetY = 0.0F
    var offsetZ = 0.0F
    var speed = 1.0F
    var amount = 1

}