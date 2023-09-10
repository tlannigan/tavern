package com.tlannigan.tavern.database

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.tlannigan.tavern.models.Config
import org.bson.UuidRepresentation

object MongoDb : Database {

    private lateinit var client: MongoClient
    lateinit var db: MongoDatabase

    override fun onEnable() {
        val (host, user, pass, name) = Config.databaseConnection
        val connectionString =
            ConnectionString("mongodb+srv://$user:$pass@$host/?retryWrites=true&w=majority")

        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()

        val settings: MongoClientSettings = MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyConnectionString(connectionString)
            .serverApi(serverApi)
            .build()

        client = MongoClient.create(settings)
        db = client.getDatabase(name ?: "tavern")
    }

    override fun onDisable() {
        client.close()
    }

}