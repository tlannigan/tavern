package com.tlannigan.tavern.models

import com.tlannigan.tavern.extensions.toTLocation
import org.bukkit.entity.Player
import java.util.*

data class Character(
    val id: UUID,
    var name: String,
    var state: PlayerState,
    var inSession: Boolean = false
) {
    companion object {
        fun create(player: Player, characterName: String? = null): Character {
            return Character(
                id = player.uniqueId,
                name = if (!characterName.isNullOrBlank()) characterName else player.displayName().toString(),
                state = PlayerState(
                    health = 20.0,
                    mana = 20,
                    location = player.location.toTLocation(),
                    inventory = Serializer.emptyInventoryBase64
                )
            )
        }
    }
}