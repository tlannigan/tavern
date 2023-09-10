package com.tlannigan.tavern.extensions

import com.tlannigan.tavern.exceptions.TavernException
import com.tlannigan.tavern.models.PlayerState
import com.tlannigan.tavern.models.TLocation
import com.tlannigan.tavern.models.TPlayer
import com.tlannigan.tavern.services.GameService
import org.bukkit.entity.Player

val Player.tavern: TPlayer
    get() = getTPlayer()

val Player.state: PlayerState
    get() = PlayerState(
        this.health,
        this.foodLevel,
        this.location.toTLocation()
    )

fun Player.getTPlayer(): TPlayer {
    return GameService.players.get(uniqueId)
        ?: throw TavernException("PlayerService did not contain TPlayer")
}

fun Player.toTPlayer(): TPlayer {
    return TPlayer(
        this.uniqueId,
        this.state
    )
}

fun Player.teleport(location: TLocation) {
    this.teleport(location.toBukkitLocation())
}