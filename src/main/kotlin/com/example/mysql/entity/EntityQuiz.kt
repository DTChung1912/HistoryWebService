package com.example.mysql.entity

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object EntityQuiz : Table<Nothing>(tableName = "quiz") {
    val quiz_id = int(name = "quiz_id").primaryKey()
    val question = varchar(name = "question")
    val theme_id = int(name = "theme_id")
    val image = varchar(name = "image")
    val answer_id = int(name = "answer_id")
}