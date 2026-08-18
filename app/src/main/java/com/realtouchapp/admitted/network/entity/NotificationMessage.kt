package com.realtouchapp.admitted.network.entity

data class NotificationMessage(
    val actionType: Int,
    val content: String,
    val createTimestamp: String,
    val notificatinoId: Int,
    val readTimestamp: String,
    val title: String
)