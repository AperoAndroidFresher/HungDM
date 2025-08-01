package com.example.hungdm.model

import android.net.Uri
import kotlinx.serialization.Serializable

//@Serializable
data class UserInfo(
    var username: String="",
    var password: String="",
    var pass2: String = "",
    var name: String ="",
    var phone: String ="",
    var email: String = "",
    var uni: String ="",
    var desc: String ="",
    var inputValid: InputValid = InputValid(),
    var imgUri: Uri? = null
)

fun noSpace(input: String): Boolean {
    return !input.contains("\\s".toRegex())
}

fun isValid(s: String): Boolean {
    return s.matches("^[a-zA-Z0-9]+$".toRegex()) && noSpace(s)
}

fun isValidPass(password: String): Boolean {
    return password.matches("^[a-zA-Z0-9]+$".toRegex()) && noSpace(password)
}

fun isValidPhone(str: String): Boolean {
    val regex = Regex("^\\d+$")
    return regex.matches(str) && str.isNotEmpty()
}

fun isValidEmail(email: String): Boolean {
    val regex = "^[a-zA-Z0-9._-]+@apero\\.vn$".toRegex()
    return email.matches(regex) && noSpace(email)
}

enum class InfoName{
    USERNAME,PASSWORD,PASS2,NAME,PHONE,EMAIL,UNI,DESC
}
