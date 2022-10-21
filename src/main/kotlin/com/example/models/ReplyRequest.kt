package com.example.models

import com.google.gson.annotations.SerializedName

data class ReplyRequest(
    @field:SerializedName("reply_id")
    val reply_id: Int? = null,

    @field:SerializedName("comment_id")
    val comment_id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: String? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("date_submitted")
    val date_submitted: String? = null,

    @field:SerializedName("like_count")
    val like_count: Int? = null,

    @field:SerializedName("dislike_count")
    val dislike_count: Int? = null
)