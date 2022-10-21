package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityAccountType : Table<Nothing>(tableName = "account_type") {
    val account_type_id = int(name = "account_type_id").primaryKey()
    val account_type_name = varchar(name = "account_type_name")
    val account_type_image = varchar(name = "account_type_image")
}