package com.example.hungdm.mvi

import android.content.Context
import com.example.hungdm.data.remote.musicApi.dto.TopAlbums
import com.example.hungdm.data.remote.musicApi.dto.TopArtists
import com.example.hungdm.data.remote.musicApi.dto.TopTracks
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import com.example.hungdm.domain.model.UserInfo

data class MviState(
    val darkTheme: Boolean = true,
    val userInfo: UserInfo = UserInfo(),
    val listSongLocal: List<Song> = mutableListOf(),
    val listSongRemote: List<Song> = mutableListOf(),
    val playlists: List<Playlist> = mutableListOf(),

    val topAlbums: TopAlbums? = null,
    val topTracks: TopTracks? = null,
    val topArtists: TopArtists? = null,

    val playerPlaylist: Playlist? = null,
    val playerListSong: List<Song>? = null,
    val playerSongIndex: Int? = null,
    val playerSong: Song? = null,
    val playerTime: Long = 0,
    val isPlay: Boolean = false,
    val isShuffle: Boolean = false,
    val isRepeat: Boolean = false
)

sealed interface MviIntent{
    data class GetUser(val userId: Long) : MviIntent
    data object OnClickSignup : MviIntent
    data class CheckLogin(val userInfo: UserInfo, val context: Context) : MviIntent
    data class CheckSignup(val userInfo: UserInfo) : MviIntent
    data object OnClickProfile: MviIntent
    data class EditProfile(val userInfo: UserInfo) : MviIntent
    data class OnLogout(val context: Context): MviIntent
    data object OnClickSetting: MviIntent

    data class LoadMusicData(val context: Context): MviIntent
    data object OnClickSeeAllTopAlbums: MviIntent
    data object OnClickSeeAllTopTracks: MviIntent
    data object OnClickSeeAllTopArtists: MviIntent

    data class LoadSongLocal(val context: Context) : MviIntent
    data class LoadSongRemote(val context: Context) : MviIntent
    data object LoadPlaylistsOfUser: MviIntent
    data class CreatePlaylist(val title: String): MviIntent
    data class RenamePlaylist(val title: String, val playlist: Playlist): MviIntent
    data class RemovePlaylist(val playlist: Playlist): MviIntent
    data class AddSongToPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class RemoveSongInPlaylist(val song: Song, val playlist: Playlist): MviIntent
    data class OnClickPlaylistDetail(val playlistId: Long) : MviIntent

    data class OnClickPlayer(val song: Song, val playerListSong: List<Song>?, val playerPlaylist: Playlist?) : MviIntent
    data object OnClickClosePlayer: MviIntent
    data object OnChangeSongPlayState: MviIntent
    data object OnClickNextSong: MviIntent
    data object OnClickPreviousSong: MviIntent
    data object OnClickShuffle: MviIntent
    data object OnClickRepeat: MviIntent
    data class OnSeek(val position: Int): MviIntent

    data object ChangeTheme : MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
    data class GotoPlaylistDetail(val playlistId: Long): MviEvent
    data object GotoTopAlbums: MviEvent
    data object GotoTopTracks: MviEvent
    data object GotoTopArtists: MviEvent
    data object GotoSettings: MviEvent
    data class ShowToast(val mess: String): MviEvent
}