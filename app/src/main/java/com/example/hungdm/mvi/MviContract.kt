package com.example.hungdm.mvi

import android.content.Context
import android.net.Uri
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.InfoName
import com.example.hungdm.model.Song
import com.example.hungdm.model.UserInfo
import com.example.hungdm.navigation.Destination

data class MviState(
    var selectedBottomBar: Int = 0,
    var darkTheme: Boolean = true,
    val linearListMusic: Boolean = true,
    var isEdit: Boolean = false,
    var showPopup: Boolean = false,
    val isLoadSong: Boolean = false,
    val showCreatePlaylistDialod: Boolean = false,
    var userInfo: UserInfo = UserInfo(),
    val selectedPlaylist: Playlist? = null,
    var backStack: MutableList<Destination> = mutableListOf(Destination.Login),
    val listSong: MutableList<Song> = mutableListOf<Song>(),
    val playlists: MutableList<Playlist> = mutableListOf()
)

sealed interface MviIntent{
    data object OnSignupClicked : MviIntent
    data object CheckLogin : MviIntent
    data object CheckSignup : MviIntent
    data object ChangeTheme : MviIntent
    data class ClickItemBottomBar(val index: Int): MviIntent
    data object OnClickProfile: MviIntent
    data object OnClickEditProfile: MviIntent
    data class OnChangeAvatar(val uri: Uri): MviIntent
    data object CheckEditProfile : MviIntent
    data object OnChangeTypeListMusic: MviIntent
    data class LoadSong(val context: Context) : MviIntent
    data class RemoveSong(val index: Int): MviIntent
    data object ShowCreatePlaylistDialod: MviIntent
    data class CreatePlaylist(val title: String): MviIntent
    data class AddSongToPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class ShowPlaylistDetail(val playlist: Playlist) : MviIntent
    data class OnChangedInput(val s: String, val infoName: InfoName) : MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
    data object GotoPlaylist: MviEvent
    data object GotoPlaylistDetail: MviEvent
}




