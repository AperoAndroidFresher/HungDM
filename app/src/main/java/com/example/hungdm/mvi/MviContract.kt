package com.example.hungdm.mvi

import android.content.Context
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.model.UserInfo
import com.example.hungdm.navigation.Destination

data class MviState(
    var selectedBottomBar: Int = 0,
    var darkTheme: Boolean = true,
    val isLoadSong: Boolean = false,
    var userInfo: UserInfo = UserInfo(),
    val selectedPlaylist: Playlist? = null,
    var backStack: MutableList<Destination> = mutableListOf(Destination.Login),
    val listSong: MutableList<Song> = mutableListOf<Song>(),
    val playlists: MutableList<Playlist> = mutableListOf(Playlist("Playlist 1", title = "Test playlist", listSong = mutableListOf()))
)

sealed interface MviIntent{
    data object OnClickSignup : MviIntent
    data class CheckLogin(val userInfo: UserInfo) : MviIntent
    data class CheckSignup(val userInfo: UserInfo) : MviIntent
    data object OnClickProfile: MviIntent
    data class CheckEditProfile(val userInfo: UserInfo) : MviIntent
    data class LoadSong(val context: Context) : MviIntent
    data class CreatePlaylist(val title: String): MviIntent
    data class RemovePlaylist(val playlist: Playlist): MviIntent
    data class AddSongToPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class RemoveSongInPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class OnClickPlaylistDetail(val playlist: Playlist) : MviIntent
    data object ChangeTheme : MviIntent
    data class OnClickItemBottomBar(val index: Int): MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
    data object GotoPlaylist: MviEvent
    data object GotoPlaylistDetail: MviEvent
}




