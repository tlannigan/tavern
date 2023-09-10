package com.tlannigan.tavern.repositories

import com.tlannigan.tavern.database.MongoDb
import com.tlannigan.tavern.models.Campaign

class CampaignRepository : Repository<Campaign>() {
    override val collection = MongoDb.db.getCollection<Campaign>("campaigns")
}