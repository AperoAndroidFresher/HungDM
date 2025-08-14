package com.example.hungdm.mvi

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.data.db.entity.PlaylistEntity
import com.example.hungdm.data.db.entity.PlaylistSongReference
import com.example.hungdm.data.mapper.toSong
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import com.example.hungdm.domain.repo.PlaylistRepository
import com.example.hungdm.domain.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.hungdm.data.mapper.toSongEntity
import com.example.hungdm.data.mapper.toUserEntity
import com.example.hungdm.data.mapper.toUserInfo
import com.example.hungdm.data.remote.musicApi.ApiMusicClient
import com.example.hungdm.data.remote.musicApi.dto.TopAlbums
import com.example.hungdm.data.remote.musicApi.dto.TopArtists
import com.example.hungdm.data.remote.musicApi.dto.TopTracks
import com.example.hungdm.data.remote.songApi.ApiSongClient
import com.example.hungdm.domain.model.UserInfo
import com.example.hungdm.domain.repo.MusicRepository
import com.example.hungdm.service.AppService
import com.example.hungdm.service.AppService.Companion.playerListSong
import com.example.hungdm.service.AppService.Companion.playerPlaylist
import com.example.hungdm.utils.AppUtils
import kotlinx.coroutines.flow.update
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException

class MviViewModel(
    private val userRepository: UserRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicRepository: MusicRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MviState())
    val state: StateFlow<MviState> = _state.asStateFlow()
    private val _event = MutableSharedFlow<MviEvent>()
    val event: SharedFlow<MviEvent> = _event.asSharedFlow()

    init {
        viewModelScope.launch {
            AppService.playerPlaylist.collect { playerPlaylist ->
                _state.update { it.copy(playerPlaylist = playerPlaylist) }
            }
        }
        viewModelScope.launch {
            AppService.playerListSong.collect { playerListSong ->
                _state.update { it.copy(playerListSong = playerListSong) }
            }
        }
        viewModelScope.launch {
            AppService.playerSong.collect { playerSong ->
                _state.update { it.copy(playerSong = playerSong) }
            }
        }
        viewModelScope.launch {
            AppService.playerSongIndex.collect { playerSongIndex ->
                _state.update { it.copy(playerSongIndex = playerSongIndex) }
            }
        }
        viewModelScope.launch {
            AppService.playerTime.collect { playerTime ->
                _state.update { it.copy(playerTime = playerTime) }
            }
        }
        viewModelScope.launch {
            AppService.isPlay.collect { isPlay ->
                _state.update { it.copy(isPlay = isPlay) }
            }
        }
        viewModelScope.launch {
            AppService.isShuffle.collect { isShuffle ->
                _state.update { it.copy(isShuffle = isShuffle) }
            }
        }
        viewModelScope.launch {
            AppService.isRepeat.collect { isRepeat ->
                _state.update { it.copy(isRepeat = isRepeat) }
            }
        }
    }

    fun processIntent(intent: MviIntent) {
        viewModelScope.launch {
            when (intent) {
                is MviIntent.GetUser -> {
                    getUser(intent.userId)
                }

                is MviIntent.OnClickSignup -> sendEvent(MviEvent.GotoSignup)


                is MviIntent.CheckLogin -> checkLogin(intent.userInfo, intent.context)


                is MviIntent.CheckSignup -> checkSignup(intent.userInfo)


                is MviIntent.OnClickProfile -> sendEvent(MviEvent.GotoProfile)


                is MviIntent.EditProfile -> editProfile(intent.userInfo)


                is MviIntent.OnLogout -> logout(intent.context)

                is MviIntent.OnClickSetting -> {
                    sendEvent(MviEvent.GotoSettings)
                }

                is MviIntent.ChangeTheme -> {
                    _state.value = _state.value.copy(darkTheme = !_state.value.darkTheme)
                }

                is MviIntent.LoadMusicData -> {
                    _state.value = _state.value.copy(
                        topAlbums = getTopAlbums(),
                        topTracks = getTopTracks(),
                        topArtists = getTopArtists()
                    )
                }

                is MviIntent.OnClickSeeAllTopAlbums -> {
                    sendEvent(MviEvent.GotoTopAlbums)
                }

                is MviIntent.OnClickSeeAllTopTracks -> {
                    sendEvent(MviEvent.GotoTopTracks)
                }

                is MviIntent.OnClickSeeAllTopArtists -> {
                    sendEvent(MviEvent.GotoTopArtists)
                }

                is MviIntent.LoadPlaylistsOfUser -> {
                    _state.update { it.copy(playlists = loadPlaylistOfUser()) }
                }

                is MviIntent.LoadSongLocal -> {
                    if(_state.value.listSongLocal.isEmpty()){
                        val songs = withContext(Dispatchers.IO) {
                            getSongExternal(intent.context)
                        }
                        _state.value = _state.value.copy(listSongLocal = songs)
                    }
                }

                is MviIntent.LoadSongRemote -> loadSongRemote(intent.context)


                is MviIntent.CreatePlaylist -> createPlaylist(intent.title)


                is MviIntent.RenamePlaylist -> {
                    playlistRepository.renamePlaylist(intent.playlist.id, intent.title)
                    _state.update { it.copy(playlists = loadPlaylistOfUser()) }
                }

                is MviIntent.RemovePlaylist -> removePlaylist(intent.playlist)


                is MviIntent.AddSongToPlaylist -> addSongToPlaylist(intent.song, intent.playlist)


                is MviIntent.RemoveSongInPlaylist -> {
                    removeSongInPlaylist(intent.song, intent.playlist)
                }

                is MviIntent.OnClickPlaylistDetail -> {
                    sendEvent(MviEvent.GotoPlaylistDetail(intent.playlistId))
                }

                is MviIntent.OnClickPlayer -> {
                    val playerSongIndex = intent.playerListSong?.indexOf(intent.song)
                        ?: intent.playerPlaylist!!.listSong.indexOf(intent.song)
                    musicRepository.playSong(
                        intent.song,
                        intent.playerListSong,
                        intent.playerPlaylist,
                        playerSongIndex
                    )
                }

                is MviIntent.OnChangeSongPlayState -> {
                    if (_state.value.isPlay) {
                        musicRepository.pause()
                    } else {
                        musicRepository.resume()
                    }
                }

                is MviIntent.OnClickNextSong -> musicRepository.next()

                is MviIntent.OnClickPreviousSong -> musicRepository.previous()

                is MviIntent.OnSeek -> musicRepository.seek(intent.position)

                is MviIntent.OnClickShuffle -> musicRepository.shuffle()

                is MviIntent.OnClickRepeat -> musicRepository.repeat()

                is MviIntent.OnClickClosePlayer -> musicRepository.close()
            }
        }
    }

    private fun sendEvent(event: MviEvent) {
        viewModelScope.launch {
            _event.emit(event)
        }
    }

    private suspend fun getUser(userId: Long) {
        val user = withContext(Dispatchers.IO) {
            userRepository.getUserById(userId)
        }
        _state.value = _state.value.copy(userInfo = user!!.toUserInfo())
    }

    private suspend fun checkLogin(userInfo: UserInfo, context: Context) {
        val user = withContext(Dispatchers.IO) {
            userRepository.login(userInfo.username, userInfo.password)
        }
        if (user != null) {
            _state.value = _state.value.copy(userInfo = user.toUserInfo())
            AppUtils.saveUser(context, user.userId)
            sendEvent(MviEvent.GotoHome)
        } else {
            sendEvent(MviEvent.ShowToast("Login failed"))
        }
    }

    private suspend fun checkSignup(userInfo: UserInfo) {
        try {
            val result = withContext(Dispatchers.IO) {
                userRepository.signup(
                    username = userInfo.username,
                    password = userInfo.password,
                    email = userInfo.email
                )
            }
            if (result > 0) {
                sendEvent(MviEvent.GotoLogin)
            } else {
                sendEvent(MviEvent.ShowToast("Username already exists"))
            }
        } catch (e: Exception) {
            sendEvent(MviEvent.ShowToast("Signup failed ${e.message}"))
        }
    }

    private suspend fun editProfile(userInfo: UserInfo) {
        withContext(Dispatchers.IO) {
            userRepository.updateUser(userInfo.toUserEntity())
        }
        _state.value = _state.value.copy(userInfo = userInfo)
    }

    private fun logout(context: Context) {
        AppUtils.clear(context)
        AppUtils.setAppLanguage("en", context)
        musicRepository.close()
        _state.value = MviState()
        sendEvent(MviEvent.GotoLogin)
    }

    private suspend fun loadSongRemote(context: Context){
        val dir = File(context.filesDir, _state.value.userInfo.username)
        if (!dir.exists()) {
            val songApi = getSongRemote()
            //delay(1000) // neu k co delay thi songApi = null, vi interface ApiService dunng Call<List<SongRemote>>
            val songInternal = mutableListOf<Song>()
            withContext(Dispatchers.IO) {
                for (i in songApi) {
                    songInternal.add(
                        downloadSongToInternalStorage(
                            context, i.path!!, _state.value.userInfo.username, i.title + ".mp3"
                        )!!
                    )
                }
            }
            _state.value = _state.value.copy(listSongRemote = songInternal)
        } else {
            val songs =
                getALlSongInternal(context, _state.value.userInfo.username)
            _state.value = _state.value.copy(listSongRemote = songs)
        }
    }

    private suspend fun createPlaylist(title: String){
        val playlistEntity = PlaylistEntity(
            title = title,
            userId = _state.value.userInfo.id
        )
        playlistRepository.createPlaylist(playlistEntity)
        _state.value = _state.value.copy(playlists = loadPlaylistOfUser())
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

    private suspend fun removeSongInPlaylist(song: Song, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistRepository.removeSongInPlaylist(song.id, playlist.id)
            _state.update { it.copy(playlists = loadPlaylistOfUser()) }
            if (_state.value.playerPlaylist?.id == playlist.id) {
                val newPlaylist = _state.value.playlists.find {
                    it.id == playlist.id
                }
                if (song.id == _state.value.playerSong?.id) {
                    if(newPlaylist!!.listSong.isEmpty()){
                        musicRepository.close()
                    } else {
                        musicRepository.updatePlaylist(newPlaylist)
                        musicRepository.handleCurrentSongDeleted()
                    }
                } else {
                    musicRepository.updatePlaylist(newPlaylist!!)
                }
            }
        }
    }

    private suspend fun addSongToPlaylist(song: Song, playlist: Playlist) {
        withContext(Dispatchers.IO) {
            val id = playlistRepository.addSong(song.toSongEntity())
            playlistRepository.addSongToPlaylist(
                PlaylistSongReference(playlist.id, id)
            )
            _state.update { it.copy(playlists = loadPlaylistOfUser()) }
            if (_state.value.playerPlaylist?.id == playlist.id) {
                val newPlaylist = _state.value.playlists.find {
                    it.id == playlist.id
                }
                musicRepository.updatePlaylist(newPlaylist!!)
            }
        }
    }

    private suspend fun removePlaylist(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistRepository.removePlaylist(playlist.id)
            playlistRepository.removeAllSongInPlaylist(playlist.id)
            _state.update { it.copy(playlists = loadPlaylistOfUser()) }
            if (_state.value.playerPlaylist?.id == playlist.id) {
                musicRepository.close()
            }
        }
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
                    val img = AppUtils.getAlbumArt(context, albumId)
                    songs.add(Song(id, title, artist, duration, songUri, img))
                }
            }
        }
        return songs
    }

    private suspend fun getALlSongInternal(
        context: Context,
        folderName: String
    ): MutableList<Song> {
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
                    val artist =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                            ?: "Unknown Artist"
                    val duration =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
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
        return@withContext try {
            ApiSongClient.build().getSongRemote().map { it.toSong() }.toMutableList()
        } catch (e: UnknownHostException) {
            Log.d("API_ERROR", "Khong co mang: ${e.message}")
            mutableListOf()
        } catch (e: IOException) {
            Log.d("API_ERROR", "Loi IO: ${e.message}")
            mutableListOf()
        } catch (e: Exception) {
            Log.d("API_ERROR", "Loi khac: ${e.message}")
            mutableListOf()
        }
    }

    private suspend fun getTopAlbums(): TopAlbums? = withContext(Dispatchers.IO) {
        return@withContext try {
            ApiMusicClient.build().getTopAlbums().topalbums
        } catch (e: UnknownHostException) {
            Log.d("API_ERROR", "Khong co mang: ${e.message}")
            null
        } catch (e: IOException) {
            Log.d("API_ERROR", "Loi IO: ${e.message}")
            null
        } catch (e: Exception) {
            Log.d("API_ERROR", "Loi khac: ${e.message}")
            null
        }
    }

    private suspend fun getTopTracks(): TopTracks? = withContext(Dispatchers.IO) {
        return@withContext try {
            ApiMusicClient.build().getTopTracks().toptracks
        } catch (e: UnknownHostException) {
            Log.d("API_ERROR", "Khong co mang: ${e.message}")
            null
        } catch (e: IOException) {
            Log.d("API_ERROR", "Loi IO: ${e.message}")
            null
        } catch (e: Exception) {
            Log.d("API_ERROR", "Loi khac: ${e.message}")
            null
        }
    }

    private suspend fun getTopArtists(): TopArtists? = withContext(Dispatchers.IO) {
        return@withContext try {
            ApiMusicClient.build().getTopArtists().topArtists
        } catch (e: UnknownHostException) {
            Log.d("API_ERROR", "Khong co mang: ${e.message}")
            null
        } catch (e: IOException) {
            Log.d("API_ERROR", "Loi IO: ${e.message}")
            null
        } catch (e: Exception) {
            Log.d("API_ERROR", "Loi khac: ${e.message}")
            null
        }
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
            withContext(Dispatchers.IO) {
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

                val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                    ?: file.nameWithoutExtension
                val artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                    ?: "Unknown Artist"
                val duration =
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.toLongOrNull() ?: 0L
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
            song
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}