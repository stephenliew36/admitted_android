package com.realtouchapp.admitted.network.entity

data class Login(
    val agreeTime: String,
    val msgDesc: String,
    val msgTitle: String,
    val name: Any,
    val success: Int,
    val token: String,
    val url: String
)