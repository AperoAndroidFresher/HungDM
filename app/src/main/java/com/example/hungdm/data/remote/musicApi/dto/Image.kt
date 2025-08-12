package com.example.hungdm.data.remote.musicApi.dto

import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("#text")
    val url: String="",
    val size: String=""
)