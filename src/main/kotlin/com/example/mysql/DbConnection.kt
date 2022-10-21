package com.example.mysql

import org.ktorm.database.Database

object DbConnection {
    private val db: Database? = null
    fun getDatabaseInstance(): Database {
        return db ?: Database.connect(
            url = "jdbc:mysql://127.0.0.1:3306/history",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "root",
            password = ""
        )
    }
}