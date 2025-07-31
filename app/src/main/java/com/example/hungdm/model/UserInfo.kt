package com.example.hungdm.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    var username: String="",
    var password: String="",
    var pass2: String = "",
    var name: String ="",
    var phone: String ="",
    var email: String = "",
    var uni: String ="",
    var desc: String ="",
    var inputValid: InputValid = InputValid(
        userValid = isValid(username),
        passValid = isValidPass(password),
        pass2valid = isValidPass(password) && password == pass2,
        emailValid = isValidEmail(email),
        nameValid = isValid(name),
        phoneValid = isValidPhone(phone),
        uniValid = isValid(uni)
    )
){
}

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
