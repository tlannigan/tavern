package com.tlannigan.tavern.models

import com.tlannigan.tavern.exceptions.TavernException
import com.tlannigan.tavern.extensions.state
import com.tlannigan.tavern.services.GameService
import org.bson.codecs.pojo.annotations.BsonId
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.*

data class TPlayer(
    @BsonId
    override val id: UUID,
    var state: PlayerState,
    var activeCampaign: UUID? = null,
    val campaigns: MutableList<UUID> = mutableListOf()
) : Document {
    private fun getPlayer(): Player {
        return Bukkit.getPlayer(this.id) ?: throw TavernException("TPlayer not unloaded from PlayerService onPlayerQuit")
    }

    fun addCampaign(id: UUID) {
        campaigns.add(id)
    }

    private fun getCampaignsWithName(campaignName: String): List<Campaign> {
        val campaignsWithName = mutableListOf<Campaign>()
        campaigns.forEach { campaignId ->
            val campaign = GameService.campaigns.get(campaignId)
            if (campaign?.name == campaignName) {
                campaignsWithName.add(campaign)
            }
        }
        return campaignsWithName
    }

    private fun hasCampaignWithName(campaignName: String): Boolean {
        return getCampaignsWithName(campaignName).isNotEmpty()
    }

    private fun hasActiveCampaign(): Boolean {
        return activeCampaign != null
    }

    private fun getActiveCampaign(): Campaign? {
        return activeCampaign?.let { GameService.campaigns.get(it) }
    }

    private fun saveOverworldState(state: PlayerState) {
        this.state = state
    }

    fun createCampaign(campaignName: String) {
        val player = getPlayer()
        if (hasActiveCampaign()) {
            player.sendMessage("Leave your current campaign to create a new one!")
            return
        }

        if (hasCampaignWithName(campaignName)) {
            player.sendMessage("Campaigns you own must have unique names, try another one.")
            return
        }

        val campaign = Campaign.create(player, campaignName)
        GameService.campaigns.load(campaign)
        addCampaign(campaign.id)
    }

    fun enterCampaign(campaignName: String) {
        val player = getPlayer()
        if (hasActiveCampaign()) {
            player.sendMessage("Leave your current campaign to enter another one.")
            return
        }

        if (campaigns.isEmpty()) {
            player.sendMessage("You are not part of any campaigns.")
            return
        }

        if (!hasCampaignWithName(campaignName)) {
            player.sendMessage("Error in campaign name.")
            return
        }

        val campaigns = getCampaignsWithName(campaignName)
        if (campaigns.size == 1) {
            saveOverworldState(player.state)
            val campaign = campaigns[0]
            campaign.admit(player)
            this.activeCampaign = campaign.id
        } else if (campaigns.size > 1) {
            // TODO Send campaign options in chat with command buttons
        } else {
            player.sendMessage("Bad things happened if you got this message.")
        }
    }

    fun leaveCampaign() {
        val player = getPlayer()
        if (!hasActiveCampaign()) {
            player.sendMessage("You are not in a campaign!")
            return
        }

        getActiveCampaign()?.let { campaign ->
            campaign.withdraw(player)
            this.activeCampaign = null
        }
    }
}