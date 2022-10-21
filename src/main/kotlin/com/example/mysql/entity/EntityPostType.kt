package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityPostType : Table<Nothing>(tableName = "post_type") {
    val post_type_id = int(name = "post_type_id").primaryKey()
    val post_type_name = varchar(name = "post_type_name")
}