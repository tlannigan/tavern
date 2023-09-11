package com.tlannigan.tavern.database

data class DatabaseConnection(
    val host: String? = "",
    val user: String? = "",
    val pass: String? = "",
    val name: String?
)
