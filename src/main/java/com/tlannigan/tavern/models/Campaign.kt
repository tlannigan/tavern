package com.tlannigan.tavern.models

import com.tlannigan.tavern.extensions.tavern
import com.tlannigan.tavern.extensions.teleport
import com.tlannigan.tavern.extensions.toTLocation
import org.bson.codecs.pojo.annotations.BsonId
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class Campaign(
    @BsonId
    override val id: UUID,
    var gameMaster: Character,
    val characters: MutableList<Character> = mutableListOf(),
    var name: String,
    var spawn: TLocation,
    var isPublic: Boolean = false,
    var inSession: Boolean = true,
    var playerLimit: Int = 8,
    var sessionState: SessionState = SessionState.FREE,
    var statSelection: StatSelection = StatSelection.ROLL,
    val createdAt: Date
) : Document {
    /**
     * Teleports player to last saved character location and sets
     * inSession to true
     */
    fun admit(player: Player) {
        getCharacter(player.uniqueId)?.let { character ->
            if (isGameMaster(character.id)) {
                startSession()
            }
            player.sendMessage("Entering campaign $name.")
            player.teleport(character.state.location)
            character.inSession = true
        }
    }

    /**
     * Teleports player to last saved overworld location and sets
     * inSession to false
     */
    fun withdraw(player: Player) {
        getCharacter(player.uniqueId)?.let { character ->
            if (!character.inSession) {
                return
            }
            if (isGameMaster(character.id)) {
                endSession()
            }
            player.sendMessage("Exiting campaign $name.")
            player.teleport(player.tavern.state.location)
            character.inSession = false
        }
    }

    /**
     * Makes the campaign joinable for all players
     */
    private fun startSession() {
        inSession = true
    }

    /**
     * Prevents players from joining and teleports all players out
     */
    private fun endSession() {
        inSession = false
        characters.forEach { character ->
            if (!character.inSession) {
                val player = Bukkit.getPlayer(character.id)
                player?.teleport(player.tavern.state.location)
            }
        }
    }

    private fun getCharacter(id: UUID): Character? {
        return if (gameMaster.id == id) {
            gameMaster
        } else {
            characters.find { it.id == id }
        }
    }

    private fun isGameMaster(id: UUID): Boolean {
        return gameMaster.id == id
    }

    companion object {
        fun create(player: Player, campaignName: String): Campaign {
            player.sendMessage("Creating campaign $campaignName!")
            return Campaign(
                id = UUID.randomUUID(),
                gameMaster = Character.create(player),
                name = campaignName,
                spawn = player.location.toTLocation(),
                createdAt = Date()
            )
        }
    }
}

enum class SessionState { FREE, COMBAT, FROZEN }

enum class StatSelection { ROLL, POINT_BUY }