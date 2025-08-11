package com.example.hungdm.data.remote.musicApi.dto

data class TopTracksResponse(
    val toptracks: TopTracks = TopTracks()
)

data class TopTracks(
    val track: List<Track> = emptyList()
)

data class Track(
    val name: String = "",
    val listeners: String = "",
    val artist: Artist = Artist(),
    val image: List<Image> = emptyList()
)