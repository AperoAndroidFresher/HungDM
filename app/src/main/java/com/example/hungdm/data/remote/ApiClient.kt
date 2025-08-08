package com.example.hungdm.data.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val BASE_URL = "https://static.apero.vn/"
    private var gsonConfig = GsonBuilder().create()

    private val retrofit by lazy { buildRetrofit() }

    fun build(): ApiService = retrofit.create(ApiService::class.java)

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonConfig))
            .build()
    }
}