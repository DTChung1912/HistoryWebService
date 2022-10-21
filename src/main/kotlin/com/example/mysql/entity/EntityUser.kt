package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityUser : Table<Nothing>(tableName = "user") {
    val user_id = varchar(name = "user_id").primaryKey()
    val user_name = varchar(name = "user_name")
    val user_image = varchar(name = "user_image")
    val email = varchar(name = "email")
    val birthday = varchar(name = "birthday")
    val phone_number = varchar(name = "phone_number")
    val address = varchar(name = "address")
    val last_active = varchar(name = "last_active")
    val account_type_id = int(name = "account_type_id")
    val access_id = int(name = "access_id")
}