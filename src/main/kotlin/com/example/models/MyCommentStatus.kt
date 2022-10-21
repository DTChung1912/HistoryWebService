package com.example.models

import com.google.gson.annotations.SerializedName

data class MyCommentStatus(
    @field:SerializedName("my_comment_id")
    val my_comment_id: Int? = null,
    @field:SerializedName("user_id")
    val user_id: String? = null,
    @field:SerializedName("video_id")
    val video_id: Int? = null,
    @field:SerializedName("comment_id")
    val comment_id: Int? = null,
    @field:SerializedName("isLike")
    val isLike: Int? = null
)