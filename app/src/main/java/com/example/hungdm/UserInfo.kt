package com.example.hungdm

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    var username: String="",
    var password: String="",
    var name: String ="",
    var phone: String ="",
    var email: String = "",
    var uni: String ="",
    var desc: String =""
)
