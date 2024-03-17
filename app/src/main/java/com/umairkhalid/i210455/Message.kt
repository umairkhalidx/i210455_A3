package com.umairkhalid.i210455

import java.io.Serializable

data class Message(
    val userId: String = "",
    val messageText: String = "",
    val timestamp: Long = 0,
    val audioUrl: String? = null,
    val imageUrl: String? = null,
    val fileUrl: String? = null,
    var key: String? = ""

):Serializable
