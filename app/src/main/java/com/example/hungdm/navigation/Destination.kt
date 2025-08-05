package com.example.hungdm.navigation

import androidx.navigation3.runtime.NavKey
import com.example.hungdm.model.UserInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination: NavKey {

    @Serializable
    @SerialName("Login")
//    data class Login(var user: UserInfo): Destination
    data object Login: Destination

    @Serializable
    @SerialName("Signup")
    data object Signup : Destination

    @Serializable
    @SerialName("Home")
//    data class Home(var user: UserInfo): Destination
    data object Home:Destination

    @Serializable
    @SerialName("Profile")
//    data class Profile(var user: UserInfo): Destination
    data object Profile: Destination


    @Serializable
    @SerialName("Playlist")
    data object Playlist : Destination

    @Serializable
    @SerialName("Library")
    data object Library : Destination

    @Serializable
    @SerialName("PlaylistDetail")
    data class PlaylistDetail(val playlistID: Long) : Destination
}