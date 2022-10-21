package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityMyComment : Table<Nothing>(tableName = "my_comment") {
    val my_comment_id = int(name = "my_comment_id").primaryKey()
    val user_id = varchar(name = "user_id")
    val video_id = int(name = "video_id")
    val comment_id = int(name = "comment_id")
    val isLike = int(name = "isLike")
}