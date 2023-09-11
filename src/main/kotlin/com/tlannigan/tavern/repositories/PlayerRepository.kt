package com.tlannigan.tavern.repositories

import com.tlannigan.tavern.database.MongoDb
import com.tlannigan.tavern.models.TPlayer

class PlayerRepository : Repository<TPlayer>() {
    override val collection = MongoDb.db.getCollection<TPlayer>("players")
}