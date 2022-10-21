package com.example.models
import com.google.gson.annotations.SerializedName

data class MyPostStatus(
    @field:SerializedName("my_post_id")
    val my_post_id: Int? = null,
    @field:SerializedName("user_id")
    val user_id: String? = null,
    @field:SerializedName("post_id")
    val post_id: Int? = null,
    @field:SerializedName("isRead")
    val isRead: Int? = null,
    @field:SerializedName("isDownload")
    val isDownload: Int? = null,
    @field:SerializedName("rate")
    val rate: Int? = null
)
