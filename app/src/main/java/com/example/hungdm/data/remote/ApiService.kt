package com.example.hungdm.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("techtrek/Remote_audio.json")
    fun getSongRemote(): Call<List<SongRemote>>
}