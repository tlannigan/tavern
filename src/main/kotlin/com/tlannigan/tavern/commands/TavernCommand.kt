package com.tlannigan.tavern.commands

import com.tlannigan.tavern.extensions.tavern
import com.tlannigan.tavern.services.GameService
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.arguments.SafeSuggestions
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import org.bukkit.entity.Player
import java.util.concurrent.CompletableFuture

object TavernCommand : Command {
    private val create =
        CommandAPICommand("create")
            .withArguments(GreedyStringArgument("campaign_name"))
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val name = args.get("campaign_name") as String
                player.tavern.createCampaign(name)
            })

    private val enter =
        CommandAPICommand("enter")
            .withArguments(GreedyStringArgument("campaign_name")
                .replaceSafeSuggestions(SafeSuggestions.suggestCollectionAsync { info ->
                    CompletableFuture.supplyAsync {
                        val campaignIds = (info.sender as Player).tavern.campaigns
                        GameService.campaigns.get(campaignIds).map { it.name }
                    }
                }))
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val name = args.get("campaign_name") as String
                player.tavern.enterCampaign(name)
            })

    private val join =
        CommandAPICommand("join")
            .withArguments(StringArgument("campaign_id"))
            .executesPlayer(PlayerCommandExecutor { _, _ ->
            })

    private val leave =
        CommandAPICommand("leave")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                player.tavern.leaveCampaign()
            })

    override val command: CommandAPICommand =
        CommandAPICommand("tavern")
            .withAliases("t")
            .withSubcommands(create, enter, join, leave)
}