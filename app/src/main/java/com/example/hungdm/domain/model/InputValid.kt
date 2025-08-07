package com.example.hungdm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class InputValid(
    var userValid: Boolean=true,
    var passValid: Boolean=true,
    var pass2valid: Boolean=true,
    var emailValid: Boolean=true,
    var nameValid: Boolean = true,
    var phoneValid: Boolean = true,
    var uniValid: Boolean = true
)
