package com.tlannigan.tavern.events

import com.okkero.skedule.skedule
import com.tlannigan.tavern.services.GameService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.java.JavaPlugin

class PlayerListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        plugin.skedule {
            withContext(Dispatchers.IO) {
                GameService.players.load(event.player)
            }
        }
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        plugin.skedule {
            withContext(Dispatchers.IO) {
                GameService.players.unload(event.player.uniqueId)
            }
        }
    }

}