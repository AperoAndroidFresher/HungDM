package com.example.hungdm.data.remote.musicApi

import com.example.hungdm.data.remote.musicApi.dto.TopAlbumsResponse
import com.example.hungdm.data.remote.musicApi.dto.TopArtistsResponse
import com.example.hungdm.data.remote.musicApi.dto.TopTracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMusicService {
    @GET("2.0/")
    suspend fun getTopAlbums(
        @Query("method") method: String = "artist.getTopAlbums",
        @Query("mbid") mbid: String = "f9b593e6-4503-414c-99a0-46595ecd2e23",
        @Query("api_key") apiKey: String = "e65449d181214f936368984d4f4d4ae8",
        @Query("format") format: String = "json"
    ): TopAlbumsResponse

    @GET("2.0/?method=artist.getTopTracks&format=json")
    suspend fun getTopTracks(
        @Query("api_key") apiKey: String = "e65449d181214f936368984d4f4d4ae8",
        @Query("mbid") mbid: String = "f9b593e6-4503-414c-99a0-46595ecd2e23"
    ): TopTracksResponse

    @GET("2.0/?method=chart.gettopartists&format=json")
    suspend fun getTopArtists(
        @Query("api_key") apiKey: String = "e65449d181214f936368984d4f4d4ae8"
    ): TopArtistsResponse
}