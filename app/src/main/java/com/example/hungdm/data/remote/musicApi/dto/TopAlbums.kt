package com.example.hungdm.data.remote.musicApi.dto


data class TopAlbumsResponse(
    val topalbums: TopAlbums = TopAlbums()
)

data class TopAlbums(
    val album: List<Album> = emptyList()
)

data class Album(
    val name: String = "",
    val artist: Artist = Artist(),
    val image: List<Image> = emptyList()
)