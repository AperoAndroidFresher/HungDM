package com.example.hungdm.data.remote.songApi

import retrofit2.http.GET

interface ApiSongService {
    @GET("techtrek/Remote_audio.json")
    suspend fun getSongRemote(): List<SongDTO>
}