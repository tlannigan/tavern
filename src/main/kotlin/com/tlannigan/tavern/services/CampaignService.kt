package com.tlannigan.tavern.services

import com.okkero.skedule.SynchronizationContext
import com.okkero.skedule.skedule
import com.tlannigan.tavern.Tavern
import com.tlannigan.tavern.models.Campaign
import com.tlannigan.tavern.repositories.CampaignRepository
import java.util.*

class CampaignService {
    private val campaigns: MutableList<Campaign> = mutableListOf()

    suspend fun preload() {
        campaigns.addAll(CampaignRepository().findAll())
    }

    fun load(campaign: Campaign) {
        campaigns.add(campaign)
    }

    fun unload() {
        Tavern.instance.skedule(SynchronizationContext.SYNC) {
            campaigns.forEach { campaign ->
                CampaignRepository().update(campaign)
            }
        }
    }

    fun get(id: UUID): Campaign? {
        return campaigns.find { it.id == id }
    }

    fun get(ids: List<UUID>): List<Campaign> {
        return campaigns.filter { ids.contains(it.id) }
    }
}