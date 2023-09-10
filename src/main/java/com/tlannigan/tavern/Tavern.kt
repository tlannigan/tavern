package com.tlannigan.tavern

import com.okkero.skedule.skedule
import com.tlannigan.tavern.commands.Commands
import com.tlannigan.tavern.database.MongoDb
import com.tlannigan.tavern.events.Events
import com.tlannigan.tavern.models.Config
import com.tlannigan.tavern.services.GameService
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import org.bukkit.plugin.java.JavaPlugin

class Tavern : JavaPlugin() {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this).verboseOutput(true))
        Commands.onLoad()
    }

    override fun onEnable() {
        instance = this
        Config.onEnable()
        MongoDb.onEnable()
        CommandAPI.onEnable()
        Events.onEnable()

        skedule { GameService.campaigns.preload() }
    }

    override fun onDisable() {
        GameService.campaigns.unload()
        MongoDb.onDisable()
        CommandAPI.onDisable()
    }

    companion object {
        lateinit var instance: JavaPlugin
    }
}
