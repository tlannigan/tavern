package com.tlannigan.tavern.models

import com.tlannigan.tavern.extensions.state
import org.bukkit.entity.Player
import java.util.*

open class Character(
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
                state = player.state
            )
        }
    }
}