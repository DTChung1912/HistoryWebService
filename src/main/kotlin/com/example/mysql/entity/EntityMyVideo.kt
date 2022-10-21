package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityMyVideo : Table<Nothing>(tableName = "my_video") {
    val my_video_id = int(name = "my_video_id").primaryKey()
    val user_id = varchar(name = "user_id")
    val video_id = int(name = "video_id")
    val isLike = int(name = "isLike")
    val isLater = int(name = "isLater")
    val isDownload = int(name = "isDownload")
    val isView = int(name = "isView")
    val isShare = int(name = "isShare")
    val isDontCare = int(name = "isDontCare")
    val view_time = int(name = "view_time")
    val duration = int(name = "duration")
}