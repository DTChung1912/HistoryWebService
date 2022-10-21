package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityAnswer : Table<Nothing>(tableName = "answer") {
    val answer_id = int(name = "answer_id").primaryKey()
    val correct = varchar(name = "correct")
    val incorrect_1 = varchar(name = "incorrect_1")
    val incorrect_2 = varchar(name = "incorrect_2")
    val incorrect_3 = varchar(name = "incorrect_3")
}