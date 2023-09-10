package com.tlannigan.tavern.repositories

import com.mongodb.client.model.Filters.eq
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.tlannigan.tavern.models.Document
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import java.util.*

abstract class Repository<T : Document> {
    abstract val collection: MongoCollection<T>

    suspend fun create(document: T) {
        collection.insertOne(document)
    }

    suspend fun find(id: UUID): T? {
        return collection.find(eq("_id", id)).firstOrNull()
    }

    suspend fun findAll(): List<T> {
        return collection.find().toList()
    }

    suspend fun update(document: T) {
        collection.replaceOne(eq("_id", document.id), document)
    }

    suspend fun delete(id: UUID) {
        collection.deleteOne(eq("_id", id))
    }
}