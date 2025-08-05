package com.example.hungdm.mvi

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.db.entity.PlaylistEntity
import com.example.hungdm.db.entity.PlaylistSongReference
import com.example.hungdm.db.entity.SongEntity
import com.example.hungdm.db.entity.UserEntity
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.model.UserInfo
import com.example.hungdm.model.getAlbumArt
import com.example.hungdm.navigation.Destination
import com.example.hungdm.repo.PlaylistRepository
import com.example.hungdm.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.net.toUri

class MviViewModel(
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {
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
                    val user = withContext(Dispatchers.IO) {
                        userRepository.login(intent.userInfo.username, intent.userInfo.password)
                    }

                    if (user != null) {
                        _state.value = _state.value.copy(
                            userInfo = UserInfo(
                                id = user.userId,
                                username = user.username,
                                password = user.password,
                                email = user.email,
                                name = user.name,
                                phone = user.phone,
                                uni = user.uni,
                                desc = user.desc,
                                imgUri = user.imgUri
                            )
                        )
                        sendEvent(MviEvent.GotoHome)
                    } else {
                        sendEvent(MviEvent.ShowToast("Đăng nhập thất bại"))
                    }
                }

                is MviIntent.CheckSignup -> {
                    val newUserInfo = intent.userInfo
                    try {
                        val result = withContext(Dispatchers.IO) {
                            userRepository.signup(
                                username = newUserInfo.username,
                                password = newUserInfo.password,
                                email = newUserInfo.email
                            )
                        }
                        if (result > 0) {
                            removeLast()
                            sendEvent(MviEvent.GotoLogin)
                            removeLast()
                        } else {
                            sendEvent(MviEvent.ShowToast("Username đã tồn tại"))
                        }
                    } catch (e: Exception) {
                        sendEvent(MviEvent.ShowToast("Đăng ký thất bại: ${e.message}"))
                    }
                }

                is MviIntent.OnClickProfile -> {
                    sendEvent(MviEvent.GotoProfile)
                }

                is MviIntent.CheckEditProfile -> {
                    withContext(Dispatchers.IO) {
                        userRepository.updateUser(
                            UserEntity(
                                userId = _state.value.userInfo.id,
                                username = _state.value.userInfo.username,
                                password = _state.value.userInfo.password,
                                name = _state.value.userInfo.name,
                                phone = _state.value.userInfo.phone,
                                email = _state.value.userInfo.email,
                                uni = _state.value.userInfo.uni,
                                desc = _state.value.userInfo.desc,
                                imgUri = _state.value.userInfo.imgUri
                            )
                        )
                    }
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

                is MviIntent.LoadPlaylistsOfUser -> {
                    _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
                }

                is MviIntent.LoadSong -> {
                    if (_state.value.selectedBottomBar == 1 && !_state.value.isLoadSong) {
                        viewModelScope.launch {
                            val songs = withContext(Dispatchers.IO) {
                                getAllSong(intent.context)
                            }
                            _state.value = _state.value.copy(
                                listSong = songs,
                                isLoadSong = true
                            )
                        }
                    }
                }

                is MviIntent.CreatePlaylist -> {
                    val playlistEntity = PlaylistEntity(
                        title = intent.title,
                        userId = _state.value.userInfo.id
                    )

                    playlistRepository.createPlaylist(playlistEntity)
                    _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
                }

                is MviIntent.RenamePlaylist -> {
                    playlistRepository.renamePlaylist(intent.playlist.id, intent.title)
                    _state.value = _state.value.copy(
                        playlists = loadPlaylistOfUser()
                    )
                }

                is MviIntent.RemovePlaylist -> {
                    playlistRepository.removePlaylist(intent.playlist.id)
                    playlistRepository.removeAllSongInPlaylist(intent.playlist.id)
                    _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
                }

                is MviIntent.AddSongToPlaylist -> {
                    val id = playlistRepository.addSong(
                        SongEntity(
                            title = intent.song.title,
                            artist = intent.song.artist,
                            duration = intent.song.duration,
                            albumArt = intent.song.albumArt,
                            uri = intent.song.uri,
                            albumArtUri = intent.song.albumArtUri!!
                        )
                    )

                    playlistRepository.addSongToPlaylist(
                        PlaylistSongReference(intent.playlist.id, id)
                    )
                    _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
                }

                is MviIntent.RemoveSongInPlaylist -> {
                    playlistRepository.removeSongInPlaylist(intent.song.id, intent.playlist.id)
                    _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
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

    private suspend fun getAllSong(context: Context): MutableList<Song> =
        withContext(Dispatchers.IO) {
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

                    val songUri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    val albumArtUri = ContentUris.withAppendedId(
                        "content://media/external/audio/albumart".toUri(), albumId
                    )

                    songs.add(Song(id, title, artist, duration, albumArt, songUri, albumArtUri))
                }
            }

            songs
        }

    private suspend fun loadPlaylistOfUser(): List<Playlist> = withContext(Dispatchers.IO) {
        val playlistWithSongsList =
            playlistRepository.getPlaylistsWithSongsOfUser(_state.value.userInfo.id)
        return@withContext playlistWithSongsList.map { playlistWithSongs ->
            val songs = playlistWithSongs.songs.map { songEntity ->
                Song(
                    id = songEntity.songId,
                    title = songEntity.title,
                    artist = songEntity.artist,
                    duration = songEntity.duration,
                    albumArt = songEntity.albumArt,
                    uri = songEntity.uri,
                    albumArtUri = songEntity.albumArtUri
                )
            }
            Playlist(
                id = playlistWithSongs.playlist.playlistId,
                title = playlistWithSongs.playlist.title,
                listSong = songs.toMutableList()
            )
        }
    }
}