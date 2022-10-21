package com.example.models

import com.google.gson.annotations.SerializedName

data class Video(
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
    val date_submitted: String? = null
)