package com.example.models

class MyPostResponse(
    val myPostStatusList: List<MyPostStatus>,
    val postList: List<Post>,
    val type: String,
    val size: Int
)
