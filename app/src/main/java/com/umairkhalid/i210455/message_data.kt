package com.umairkhalid.i210455

import java.io.Serializable

data class message_data(
//    val img: String?="",
    val userId: String = "",
    val mentorId: String = "",
    var messageText: String = "",
    val timestamp: Long = 0,
    var audioUrl: String? = null,
    var imageUrl: String? = null,
    var fileUrl: String? = null,
    var key: String? = "",
    var type: String? = "",
    var counter: Int = 0

):Serializable
