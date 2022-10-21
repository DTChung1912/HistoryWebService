package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityTheme : Table<Nothing>(tableName = "theme") {
    val theme_id = int(name = "theme_id").primaryKey()
    val theme_name = varchar(name = "theme_name")
    val theme_image = varchar(name = "theme_image")
}