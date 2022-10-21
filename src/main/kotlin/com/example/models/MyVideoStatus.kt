package com.example.models

import com.google.gson.annotations.SerializedName

data class MyVideoStatus(
    @field:SerializedName("my_video_id")
    val my_video_id: Int? = null,
    @field:SerializedName("user_id")
    val user_id: String? = null,
    @field:SerializedName("video_id")
    val video_id: Int? = null,
    @field:SerializedName("isLike")
    val isLike: Int? = null,
    @field:SerializedName("isLater")
    val isLater: Int? = null,
    @field:SerializedName("isDownload")
    val isDownload: Int? = null,
    @field:SerializedName("isView")
    val isView: Int? = null,
    @field:SerializedName("isShare")
    val isShare: Int? = null,
    @field:SerializedName("isDontCare")
    val isDontCare: Int? = null,
    @field:SerializedName("view_time")
    val view_time: Int? = null,
    @field:SerializedName("duration")
    val duration: Int? = null
)
