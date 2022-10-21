package com.example.models

import com.google.gson.annotations.SerializedName

data class Quiz(
    @field:SerializedName("quiz_id")
    val quiz_id: Int? = null,
    @field:SerializedName("question")
    val question: String? = null,
    @field:SerializedName("theme_id")
    val theme_id: Int? = null,
    @field:SerializedName("image")
    val image: String? = null,
    @field:SerializedName("answer_id")
    val answer_id: Int? = null,
    @field:SerializedName("correct")
    val correct: String? = null,
    @field:SerializedName("incorrect_1")
    val incorrect_1: String? = null,
    @field:SerializedName("incorrect_2")
    val incorrect_2: String? = null,
    @field:SerializedName("incorrect_3")
    val incorrect_3: String? = null
)