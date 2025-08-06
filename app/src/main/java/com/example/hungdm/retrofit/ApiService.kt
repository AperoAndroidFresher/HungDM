package com.example.hungdm.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("techtrek/Remote_audio.json")
    fun getSongRemote(): Call<List<SongRemote>>
}