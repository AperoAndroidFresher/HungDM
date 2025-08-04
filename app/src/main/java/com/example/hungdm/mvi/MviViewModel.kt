package com.example.hungdm.mvi

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.model.getAlbumArt
import com.example.hungdm.navigation.Destination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MviViewModel : ViewModel() {
    private val _state = MutableStateFlow<MviState>(MviState())
    val state: StateFlow<MviState> = _state.asStateFlow()
    private val _event = MutableSharedFlow<MviEvent>()
    val event: SharedFlow<MviEvent> = _event.asSharedFlow()

    fun processIntent(intent: MviIntent) {
        viewModelScope.launch {
            when (intent) {
                is MviIntent.OnClickSignup -> {
                    sendEvent(MviEvent.GotoSignup)
                }

                is MviIntent.CheckLogin -> {
                    //check db
                    _state.value = _state.value.copy(
                        userInfo = intent.userInfo
                    )
                    sendEvent(MviEvent.GotoHome)
                }

                is MviIntent.CheckSignup -> {
                    //check + luu vao db
                    removeLast()
                    sendEvent(MviEvent.GotoLogin)
                    removeLast()
                }

                is MviIntent.OnClickProfile -> {
                    sendEvent(MviEvent.GotoProfile)
                }

                is MviIntent.CheckEditProfile -> {
                    _state.value = _state.value.copy(
                        userInfo = intent.userInfo
                    )

                }

                is MviIntent.ChangeTheme -> {
                    _state.value = _state.value.copy(darkTheme = !_state.value.darkTheme)
                }

                is MviIntent.OnClickItemBottomBar -> {
                    _state.value = _state.value.copy(
                        selectedBottomBar = intent.index
                    )
                }


                is MviIntent.LoadSong -> {
                    if(_state.value.selectedBottomBar==1 && !_state.value.isLoadSong){
                        delay(1500)
                        _state.value = _state.value.copy(
                            listSong = getAllSong(intent.context),
                            isLoadSong = true
                        )
                    }
                }

                is MviIntent.CreatePlaylist -> {
                    val playlists = _state.value.playlists.toMutableList()
                    playlists.add(Playlist(title = intent.title))
                    _state.value = _state.value.copy(
                        playlists = playlists
                    )
                }

                is MviIntent.RenamePlaylist -> {
                    _state.value = _state.value.copy(
                        playlists = _state.value.playlists.map {
                            if(it.id==intent.playlist.id) it.copy(title = intent.title) else it
                        }
                    )
                }

                is MviIntent.RemovePlaylist -> {
                    val playlists = _state.value.playlists.toMutableList()
                    playlists.remove(intent.playlist)
                    _state.value = _state.value.copy(playlists = playlists)
                }

                is MviIntent.AddSongToPlaylist -> {
                    val playlists = _state.value.playlists.toMutableList()
                    val index = playlists.indexOfFirst { it.id == intent.playlist.id }
                    val oldPlaylist = playlists[index]
                    val newListSong = oldPlaylist.listSong.toMutableList().apply {
                        add(intent.song)
                    }
                    val newPlaylist = oldPlaylist.copy(listSong = newListSong)

                    playlists[index] = newPlaylist

                    _state.value = _state.value.copy(playlists = playlists)
                }

                is MviIntent.RemoveSongInPlaylist -> {
                    val playlists = _state.value.playlists.toMutableList()
                    val index = playlists.indexOfFirst { it.id == intent.playlist.id }
                    val oldPlaylist = playlists[index]
                    val newListSong = oldPlaylist.listSong.toMutableList().apply {
                        removeAt(intent.songIndex)
                    }
                    val newPlaylist = oldPlaylist.copy(listSong = newListSong)
                    playlists[index] = newPlaylist
                    _state.value = _state.value.copy(playlists = playlists)
                }

                is MviIntent.OnClickPlaylistDetail -> {
                    sendEvent(MviEvent.GotoPlaylistDetail(intent.playlistId))
                }
            }
        }
    }

    fun add(destination: Destination) {
        val newBackStack = _state.value.backStack.toMutableList().apply {
            add(destination)
        }
        _state.value = _state.value.copy(backStack = newBackStack)
    }

    fun removeLast() {
        val newBackStack = _state.value.backStack.toMutableList().apply {
            removeLastOrNull()
        }
        _state.value = _state.value.copy(backStack = newBackStack)
    }

    fun replace(destination: Destination) {
        val newBackStack = mutableListOf<Destination>(destination)
        _state.value = _state.value.copy(backStack = newBackStack)
    }

    private fun sendEvent(event: MviEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private fun getAllSong(context: Context): MutableList<Song> {
        val songs = mutableListOf<Song>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Albums.ALBUM_ID
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor = context.contentResolver.query(
            uri, projection, selection, null, null
        )

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val duration = it.getLong(durationColumn)
                val albumId = it.getLong(albumIdColumn)
                val albumArt = getAlbumArt(context, albumId)

                val uri =
                    ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)

                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                )

                songs.add(Song(id, title, artist, duration, albumArt, uri, albumArtUri))
            }
        }

        return songs
    }

}