package com.tlannigan.tavern.extensions

import com.tlannigan.tavern.exceptions.TavernException
import com.tlannigan.tavern.models.PlayerState
import com.tlannigan.tavern.models.Serializer
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
        this.location.toTLocation(),
        this.inventory.serialize()
    )

fun Player.updateState(state: PlayerState) {
    val (health, mana, location, inventory) = state
    this.health = health
    this.foodLevel = mana
    this.teleport(location)

    val serializer = Serializer()
    val contents = serializer.itemStackArrayFromBase64(inventory[0])
    val armor = serializer.itemStackArrayFromBase64(inventory[1])

    this.inventory.contents = contents
    this.inventory.armorContents = armor
}

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