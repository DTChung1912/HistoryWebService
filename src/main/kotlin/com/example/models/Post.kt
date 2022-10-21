package com.example.models

import com.google.gson.annotations.SerializedName

data class Post(
    @field:SerializedName("post_id")
    val post_id: Int? = null,
    @field:SerializedName("post_type_id")
    val post_type_id: Int? = null,
    @field:SerializedName("post_type_name")
    val post_type_name: String? = null,
    @field:SerializedName("theme_id")
    val theme_id: Int? = null,
    @field:SerializedName("title")
    val title: String? = null,
    @field:SerializedName("content")
    val content: String? = null,
    @field:SerializedName("pdf")
    val pdf: String? = null,
    @field:SerializedName("image")
    val image: String? = null,
    @field:SerializedName("description")
    val description: String? = null,
    @field:SerializedName("timeline")
    val timeline: String? = null,
    @field:SerializedName("place")
    val place: String? = null,
    @field:SerializedName("read_count")
    val read_count: Int? = null,
    @field:SerializedName("download_count")
    val download_count: Int? = null,
    @field:SerializedName("rate_count")
    val rate_count: Int? = null,
    @field:SerializedName("date_submitted")
    val date_submitted: String? = null
)