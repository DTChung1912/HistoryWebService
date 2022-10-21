package com.example.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @field:SerializedName("user_id")
    var user_id: String,
    @field:SerializedName("user_name")
    var user_name: String? = null,
    @field:SerializedName("user_image")
    var user_image: String? = null,
    @field:SerializedName("email")
    var email: String? = null,
    @field:SerializedName("birthday")
    var birthday: String?,
    @field:SerializedName("phone_number")
    var phone_number: String?,
    @field:SerializedName("address")
    var address: String?,
    @field:SerializedName("last_active")
    val last_active: String?,
    @field:SerializedName("account_type_id")
    val account_type_id: Int? = null,
    @field:SerializedName("access_id")
    val access_id: Int? = null
) : Serializable
