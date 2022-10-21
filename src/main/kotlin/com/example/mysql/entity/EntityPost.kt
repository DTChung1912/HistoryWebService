package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityPost : Table<Nothing>(tableName = "post") {
    val post_id = int(name = "post_id").primaryKey()
    val post_type_id = int(name = "post_type_id")
    val theme_id = int(name = "theme_id")
    val title = varchar(name = "title")
    val content = varchar(name = "content")
    val pdf = varchar(name = "pdf")
    val image = varchar(name = "image")
    val description = varchar(name = "description")
    val timeline = varchar(name = "timeline")
    val place = varchar(name = "place")
    val read_count = int(name = "read_count")
    val download_count = int(name = "download_count")
    val rate_count = int(name = "rate_count")
    val date_submitted = varchar(name = "date_submitted")
}