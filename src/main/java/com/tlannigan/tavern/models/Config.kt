package com.tlannigan.tavern.models

import com.tlannigan.tavern.Tavern
import com.tlannigan.tavern.database.DatabaseConnection
import org.bukkit.Bukkit.getLogger

object Config {

    private val plugin = Tavern.instance

    fun onEnable() {
        plugin.config.options().copyDefaults(true)
        plugin.saveDefaultConfig()
        plugin.saveConfig()
    }

    private fun getConfig(configNode: String, default: String = ""): String? {
        val config = plugin.config.getString(configNode, default)
        if (config.equals(default)) {
            getLogger().warning("Config value for '$configNode' not found, default value used instead.")
        }
        return plugin.config.getString(configNode, default)
    }

    val databaseConnection: DatabaseConnection
        get() = DatabaseConnection(
            host = getConfig("database.host", ""),
            user = getConfig("database.user", ""),
            pass = getConfig("database.pass", ""),
            name = getConfig("database.name", "tavern")
        )

}