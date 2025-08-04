package com.example.hungdm.mvi

import android.content.Context
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.model.UserInfo
import com.example.hungdm.navigation.Destination

data class MviState(
    val selectedBottomBar: Int = 0,
    val darkTheme: Boolean = true,
    val isLoadSong: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    var backStack: List<Destination> = mutableListOf(Destination.Login),
    val listSong: List<Song> = mutableListOf<Song>(),
    val playlists: List<Playlist> = mutableListOf(),
    val selectedPlaylist: Playlist? = null
)

sealed interface MviIntent{
    data object OnClickSignup : MviIntent
    data class CheckLogin(val userInfo: UserInfo) : MviIntent
    data class CheckSignup(val userInfo: UserInfo) : MviIntent
    data object OnClickProfile: MviIntent
    data class CheckEditProfile(val userInfo: UserInfo) : MviIntent
    data class LoadSong(val context: Context) : MviIntent
    data class CreatePlaylist(val title: String): MviIntent
    data class RenamePlaylist(val title: String, val playlist: Playlist): MviIntent
    data class RemovePlaylist(val playlist: Playlist): MviIntent
    data class AddSongToPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class RemoveSongInPlaylist(val songIndex: Int, val playlist: Playlist): MviIntent
    data class OnClickPlaylistDetail(val playlistId: String) : MviIntent
    data object ChangeTheme : MviIntent
    data class OnClickItemBottomBar(val index: Int): MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
    data object GotoPlaylist: MviEvent
    data class GotoPlaylistDetail(val playlistId: String): MviEvent
}




