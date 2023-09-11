package com.tlannigan.tavern.services

import com.tlannigan.tavern.extensions.toTPlayer
import com.tlannigan.tavern.models.TPlayer
import com.tlannigan.tavern.repositories.PlayerRepository
import org.bukkit.entity.Player
import java.util.*

class PlayerService {
    private val players: MutableList<TPlayer> = mutableListOf()

    /**
     * Fetches a TPlayer from a database and stores it in-memory.
     * Creates and persists a new one if not found.
     */
    suspend fun load(player: Player) {
        val tPlayer = PlayerRepository().find(player.uniqueId) ?: player.toTPlayer()
        players.add(tPlayer)
    }

    /**
     * Persists a TPlayer's current state and removes it from memory.
     */
    suspend fun unload(id: UUID) {
        players.find { it.id == id }?.let { tPlayer ->
            PlayerRepository().update(tPlayer)
            players.removeIf { it.id == id }
        }
    }

    fun get(id: UUID): TPlayer? {
        return players.find { it.id == id }
    }

    fun get(ids: List<UUID>): List<TPlayer> {
        return players.filter { ids.contains(it.id) }
    }
}