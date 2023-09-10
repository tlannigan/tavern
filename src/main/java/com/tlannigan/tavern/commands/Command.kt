package com.tlannigan.tavern.commands

import dev.jorel.commandapi.CommandAPICommand

interface Command {
    val command: CommandAPICommand
    fun register() {
        command.register()
    }
}