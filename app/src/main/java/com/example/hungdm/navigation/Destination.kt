package com.example.hungdm.navigation

import androidx.navigation3.runtime.NavKey
import com.example.hungdm.domain.model.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


sealed interface Destination: NavKey {
    data object Splash: Destination
    data object Login: Destination
    data object Signup : Destination
    data object Home: Destination
    data object Profile: Destination
    data object Playlist : Destination
    data object Library : Destination
    data class PlaylistDetail(val playlistID: Long) : Destination
    data object Player : Destination
    data object TopAlbums: Destination
    data object TopTracks: Destination
    data object TopArtists: Destination
    data object Settings: Destination
}