package com.example.hungdm.data.remote.musicApi.dto

import com.google.gson.annotations.SerializedName

data class TopArtistsResponse(
    @SerializedName("artists")
    val topArtists: TopArtists = TopArtists()
)

data class TopArtists(
    val artist: List<Artist> = emptyList()
)

data class Artist(
    val name: String = "",
    val image: List<Image> = emptyList()
)