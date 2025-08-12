package com.example.hungdm.data.remote.musicApi

import com.example.hungdm.data.remote.songApi.ApiSongService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiMusicClient {
    private const val BASE_URL = "https://ws.audioscrobbler.com/"
    private var gsonConfig = GsonBuilder().create()

    private val retrofit by lazy { buildRetrofit() }

    fun build(): ApiMusicService = retrofit.create(ApiMusicService::class.java)

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .build()
    }
}