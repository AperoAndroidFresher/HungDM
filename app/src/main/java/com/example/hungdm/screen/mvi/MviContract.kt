package com.example.hungdm.screen.mvi

import android.content.Context
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import com.example.hungdm.domain.model.UserInfo

data class MviState(
    val darkTheme: Boolean = true,
    val userInfo: UserInfo = UserInfo(),
    val listSongLocal: List<Song> = mutableListOf<Song>(),
    val listSongRemote: List<Song> = mutableListOf<Song>(),
    val playlists: List<Playlist> = mutableListOf()
)

sealed interface MviIntent{
    data object OnClickSignup : MviIntent
    data class CheckLogin(val userInfo: UserInfo, val context: Context) : MviIntent
    data class CheckSignup(val userInfo: UserInfo) : MviIntent
    data object OnClickProfile: MviIntent
    data class EditProfile(val userInfo: UserInfo) : MviIntent
    data class LoadSongLocal(val context: Context) : MviIntent
    data class LoadSongRemote(val context: Context) : MviIntent
    data class LoadSongInternal(val context: Context) : MviIntent
    data object LoadPlaylistsOfUser: MviIntent
    data class CreatePlaylist(val title: String): MviIntent
    data class RenamePlaylist(val title: String, val playlist: Playlist): MviIntent
    data class RemovePlaylist(val playlist: Playlist): MviIntent
    data class AddSongToPlaylist(val context: Context, val song: Song, val playlist: Playlist, val isDownload: Boolean): MviIntent
    data class RemoveSongInPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class OnClickPlaylistDetail(val playlistId: Long) : MviIntent
    data object ChangeTheme : MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
    data class GotoPlaylistDetail(val playlistId: Long): MviEvent
    data class ShowToast(val mess: String): MviEvent
}




