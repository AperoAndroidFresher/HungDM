package com.example.hungdm.data.remote

import com.example.hungdm.data.remote.dto.SongDTO
import retrofit2.http.GET

interface ApiService {
    @GET("techtrek/Remote_audio.json")
    suspend fun getSongRemote(): List<SongDTO>

    @GET("ApeMusic/artist/gettopalbum")
    suspend fun getTopAlbum()

    @GET("ApeMusic/artist/toptracks")
    suspend fun getTopTracks()

    @GET("ApeMusic/chart/topartist")
    suspend fun getTopArtist()
}