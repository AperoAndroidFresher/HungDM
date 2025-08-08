package com.example.hungdm.domain.model

import android.net.Uri
import kotlinx.serialization.Serializable

//@Serializable
data class UserInfo(
    var id: Long=0,
    var username: String="",
    var password: String="",
    var pass2: String = "",
    var name: String ="",
    var phone: String ="",
    var email: String = "",
    var uni: String ="",
    var desc: String ="",
    var inputValid: InputValid = InputValid(),
    var img: ByteArray? = null
)