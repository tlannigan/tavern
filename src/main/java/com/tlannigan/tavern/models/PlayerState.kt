package com.tlannigan.tavern.models

data class PlayerState(
    val health: Double,
    val mana: Int,
    val location: TLocation,
//    val inventory: Array<String>
)