package com.example.models

data class MyVideoRespone(
    val myVideoStatusList: List<MyVideoStatus>,
    val videoList: List<Video>,
    val type: String,
    val size: Int
)
