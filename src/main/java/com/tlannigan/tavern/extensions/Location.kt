package com.tlannigan.tavern.extensions

import com.tlannigan.tavern.models.TLocation
import org.bukkit.Location

fun Location.toTLocation(): TLocation {
    return TLocation(
        this.world.name,
        this.x,
        this.y,
        this.z,
        this.yaw,
        this.pitch
    )
}