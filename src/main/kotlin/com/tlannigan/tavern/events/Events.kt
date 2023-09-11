package com.tlannigan.tavern.events

import com.tlannigan.tavern.Tavern

object Events {

    private val plugin = Tavern.instance

    fun onEnable() {
        plugin.server.pluginManager.registerEvents(PlayerListener(plugin), plugin)
        plugin.server.pluginManager.registerEvents(FreezeListener(), plugin)
    }

}