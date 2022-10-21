package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityVideo : Table<Nothing>(tableName = "video") {
    val video_id = int(name = "video_id").primaryKey()
    val title = varchar(name = "title")
    val theme_id = int(name = "theme_id")
    val creater_image = varchar(name = "creater_image")
    val creater = varchar(name = "creater")
    val platform = varchar(name = "platform")
    val like_count = int(name = "like_count")
    val view_count = int(name = "view_count")
    val dislike_count = int(name = "dislike_count")
    val comment_count = int(name = "comment_count")
    val download_count = int(name = "download_count")
    val video_url = varchar(name = "video_url")
    val poster_image = varchar("poster_image")
    val date_submitted = varchar(name = "date_submitted")
}