package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityComment : Table<Nothing>(tableName = "comment") {
    val comment_id = int(name = "comment_id").primaryKey()
    val video_id = int(name = "video_id")
    val user_id = varchar(name = "user_id")
    val content = varchar(name = "content")
    val date_submitted = varchar(name = "date_submitted")
    val reply_count = int(name = "reply_count")
    val like_count = int(name = "like_count")
    val dislike_count = int(name = "dislike_count")
}