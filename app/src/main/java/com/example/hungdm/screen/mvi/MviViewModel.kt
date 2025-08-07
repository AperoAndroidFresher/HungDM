package com.example.hungdm.screen.mvi

import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.data.db.entity.PlaylistEntity
import com.example.hungdm.data.db.entity.PlaylistSongReference
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import com.example.hungdm.domain.model.getAlbumArt
import com.example.hungdm.domain.repo.PlaylistRepository
import com.example.hungdm.domain.repo.UserRepository
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
import com.example.hungdm.utils.UserPreferences
import com.example.hungdm.data.mapper.toSongEntity
import com.example.hungdm.data.mapper.toUserEntity
import com.example.hungdm.data.mapper.toUserInfo
import com.example.hungdm.data.remote.ApiClient
import com.example.hungdm.data.remote.SongRemote
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MviViewModel(
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _state = MutableStateFlow<MviState>(MviState())
    val state: StateFlow<MviState> = _state.asStateFlow()
    private val _event = MutableSharedFlow<MviEvent>()
    val event: SharedFlow<MviEvent> = _event.asSharedFlow()

    fun processIntent(intent: MviIntent) {
        viewModelScope.launch  {
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
                            userInfo = user.toUserInfo()
                        )
                        UserPreferences.saveUser(intent.context, user)
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
                            sendEvent(MviEvent.GotoLogin)
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

                is MviIntent.EditProfile -> {
                    withContext(Dispatchers.IO) {
                        userRepository.updateUser(intent.userInfo.toUserEntity())
                    }
                    _state.value = _state.value.copy(
                        userInfo = intent.userInfo
                    )
                }

                is MviIntent.ChangeTheme -> {
                    _state.value = _state.value.copy(darkTheme = !_state.value.darkTheme)
                }

                is MviIntent.LoadPlaylistsOfUser -> {
                    _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
                }

                is MviIntent.LoadSongLocal -> {
                    val songs = withContext(Dispatchers.IO) {
                        getSongExternal(intent.context)
                    }
                    _state.value = _state.value.copy(
                        listSongLocal = songs,
                    )
                }

                is MviIntent.LoadSongRemote -> {
                    val dir = File(intent.context.filesDir, _state.value.userInfo.username)
                    if (!dir.exists()) {
                        val songApi = getSongRemote()
                        delay(1000) // neu k co delay thi songApi = null, vi interface ApiService dunng Call<List<SongRemote>>
                        val songInternal = mutableListOf<Song>()
                        withContext(Dispatchers.IO){
                            for (i in songApi) {
                                songInternal.add(
                                    downloadSongToInternalStorage(
                                        intent.context,
                                        i.path!!,
                                        _state.value.userInfo.username,
                                        i.title + ".mp3"
                                    )!!
                                )
                            }
                        }
                        _state.value = _state.value.copy(listSongRemote = songInternal)
                    } else {
                        val songs = getALlSongInternal(intent.context, _state.value.userInfo.username)
                        _state.value = _state.value.copy(listSongRemote = songs)
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
                    val id = playlistRepository.addSong(intent.song.toSongEntity())
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

    private fun sendEvent(event: MviEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private suspend fun loadPlaylistOfUser(): List<Playlist> {
        val playlists = mutableListOf<Playlist>()
        withContext(Dispatchers.IO) {
            val playlistWithSongsList =
                playlistRepository.getPlaylistsWithSongsOfUser(_state.value.userInfo.id)
            playlistWithSongsList.map {
                val songs = it.songs.map { songEntity ->
                    Song(
                        id = songEntity.songId,
                        title = songEntity.title,
                        artist = songEntity.artist,
                        duration = songEntity.duration,
                        uri = songEntity.uri,
                        img = songEntity.img
                    )
                }
                playlists.add(
                    Playlist(
                        id = it.playlist.playlistId,
                        title = it.playlist.title,
                        listSong = songs.toMutableList()
                    )
                )
            }
        }
        return playlists
    }

    private suspend fun getSongExternal(context: Context): MutableList<Song> {
        val songs = mutableListOf<Song>()
        withContext(Dispatchers.IO) {
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
                    val songUri =
                        ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                    val img = getAlbumArt(context, albumId)
                    songs.add(Song(id, title, artist, duration, songUri, img))
                }
            }
        }
        return songs
    }

    private suspend fun getALlSongInternal(context: Context, folderName: String): MutableList<Song> {
        val songs = mutableListOf<Song>()
        val dir = File(context.filesDir, folderName)
        val mp3 = dir.listFiles() ?: return mutableListOf()

        withContext(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            for (i in mp3) {
                try {
                    retriever.setDataSource(i.absolutePath)
                    val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                        ?: i.nameWithoutExtension
                    val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                        ?: "Unknown Artist"
                    val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.toLongOrNull() ?: 0L
                    val path = i.absolutePath
                    val img = retriever.embeddedPicture
                    songs.add(
                        Song(
                            title = title,
                            artist = artist,
                            duration = duration,
                            uri = Uri.fromFile(i),
                            img = img,
                            path = path
                        )
                    )
                } catch (e: Exception) {
                    Log.d("getSongInternal", e.toString())
                }
            }
        }
        return songs
    }

    private suspend fun getSongRemote(): MutableList<Song> = withContext(Dispatchers.IO) {
        val songs = mutableListOf<Song>()
        val callApi = ApiClient.build().getSongRemote()
        callApi.enqueue(object : Callback<List<SongRemote>> {
            override fun onFailure(call: Call<List<SongRemote>>, t: Throwable) {
                Log.d("tag", "getSongRemote onfailure: ${t.message}")
            }

            override fun onResponse(
                call: Call<List<SongRemote>>,
                response: Response<List<SongRemote>>
            ) {
                when {
                    response.isSuccessful -> {
                        val data = response.body()
                        data?.forEach {
                            songs.add(
                                Song(
                                    title = it.title,
                                    artist = it.artist,
                                    duration = it.duration.toLong(),
                                    kind = it.kind,
                                    path = it.path
                                )
                            )
                        }
                    }
                }
            }
        })
        songs
    }

    private suspend fun downloadSongToInternalStorage(
        context: Context,
        fileUrl: String,
        folderName: String,
        fileName: String
    ): Song? {
        val dir = File(context.filesDir, folderName)
        if (!dir.exists()) dir.mkdir()
        val file = File(dir, fileName)
        if (file.exists()) return null

        return try {
            var song: Song?
            withContext(Dispatchers.IO){
                val url = URL(fileUrl)
                val connection = withContext(Dispatchers.IO) {
                    url.openConnection()
                } as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"
                connection.doInput = true
                connection.connect()

                if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                    throw IOException("HTTP error code: ${connection.responseCode}")
                }

                val inputStream = BufferedInputStream(connection.inputStream)
                val outputStream = FileOutputStream(file)

                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(file.absolutePath)

                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: file.nameWithoutExtension
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown Artist"
                val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0L
                val uri = Uri.fromFile(file)
                val img = retriever.embeddedPicture
                val path = file.absolutePath
                song = Song(
                    title = title,
                    artist = artist,
                    duration = duration,
                    uri = uri,
                    img = img,
                    path = path
                )
            }
            Log.d("tag", "downloadSongToInternalStorage: $file")
            song
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}