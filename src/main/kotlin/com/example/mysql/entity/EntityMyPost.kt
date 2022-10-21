package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityMyPost : Table<Nothing>(tableName = "my_post") {
    val my_post_id = int(name = "my_post_id").primaryKey()
    val user_id = varchar(name = "user_id")
    val post_id = int(name = "post_id")
    val isRead = int(name = "isRead")
    val isDownload = int(name = "isDownload")
    val rate = int(name = "rate")
}