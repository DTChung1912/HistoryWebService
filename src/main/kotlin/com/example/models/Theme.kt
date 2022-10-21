package com.example.models

import com.google.gson.annotations.SerializedName

class Theme (
    @field:SerializedName("theme_id")
    val theme_id: Int? = null,
    @field:SerializedName("theme_name")
    val theme_name: String? = null,
    @field:SerializedName("theme_image")
    val theme_image: String? = null
)