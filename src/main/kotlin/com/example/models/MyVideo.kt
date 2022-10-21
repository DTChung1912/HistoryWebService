package com.example.models

import com.google.gson.annotations.SerializedName

data class MyVideo(
    @field:SerializedName("my_video_id")
    val my_video_id: Int? = null,
    @field:SerializedName("user_id")
    val user_id: String? = null,
    @field:SerializedName("video_id")
    val video_id: Int? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("theme_id")
    val theme_id: Int? = null,
    @field:SerializedName("creater_image")
    val creater_image: String? = null,
    @field:SerializedName("creater")
    val creater: String? = null,
    @field:SerializedName("platform")
    val platform: String? = null,
    @field:SerializedName("like_count")
    val like_count: Int? = null,
    @field:SerializedName("view_count")
    val view_count: Int? = null,
    @field:SerializedName("dislike_count")
    val dislike_count: Int? = null,
    @field:SerializedName("comment_count")
    val comment_count: Int? = null,
    @field:SerializedName("download_count")
    val download_count: Int? = null,
    @field:SerializedName("video_url")
    val video_url: String? = null,
    @field:SerializedName("poster_image")
    val poster_image: String? = null,
    @field:SerializedName("date_submitted")
    val date_submitted: String? = null,
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