package com.example.hungdm

data class InputLoginSignup(
    var user: String="",
    var pass: String="",
    var pass2: String="",
    var email: String="",
    var userValid: Boolean=true,
    var passValid: Boolean=true,
    var pass2valid: Boolean=true,
    var emailValid: Boolean=true
)