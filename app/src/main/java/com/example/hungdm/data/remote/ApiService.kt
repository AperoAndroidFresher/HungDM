package com.example.hungdm.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("techtrek/Remote_audio.json")
    suspend fun getSongRemote(): List<SongRemote>
}