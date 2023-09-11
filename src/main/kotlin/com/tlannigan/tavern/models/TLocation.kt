package com.tlannigan.tavern.models

import org.bukkit.Bukkit.getWorld
import org.bukkit.Location

data class TLocation(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float,
    val pitch: Float
) {
    fun toBukkitLocation(): Location {
        return Location(getWorld(world), x, y, z, yaw, pitch)
    }
}