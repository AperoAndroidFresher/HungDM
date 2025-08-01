package com.example.hungdm.mvi

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.model.InfoName
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.model.UserInfo
import com.example.hungdm.model.getAlbumArt
import com.example.hungdm.model.isValid
import com.example.hungdm.model.isValidEmail
import com.example.hungdm.model.isValidPass
import com.example.hungdm.model.isValidPhone
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
                is MviIntent.OnChangedInput -> {
                    when (intent.infoName) {
                        InfoName.USERNAME -> {
                            val isValid = isValid(intent.s)

                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    username = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(userValid = isValid)
                                )
                            )
                        }

                        InfoName.PASSWORD -> {
                            val isValid = isValidPass(intent.s)

                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    password = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(passValid = isValid)
                                )
                            )
                        }

                        InfoName.PASS2 -> {
                            val isValid =
                                isValidPass(intent.s) && intent.s == _state.value.userInfo.password
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    pass2 = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(pass2valid = isValid)
                                )
                            )
                        }

                        InfoName.NAME -> {
                            val isValid = isValid(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    name = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(nameValid = isValid)
                                )
                            )
                        }

                        InfoName.PHONE -> {
                            val isValid = isValidPhone(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    phone = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(phoneValid = isValid)
                                )
                            )
                        }

                        InfoName.EMAIL -> {
                            val isValid = isValidEmail(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    email = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(emailValid = isValid)
                                )
                            )
                        }

                        InfoName.UNI -> {
                            val isValid = isValid(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(
                                    uni = intent.s,
                                    inputValid = _state.value.userInfo.inputValid.copy(uniValid = isValid)
                                )
                            )
                        }

                        InfoName.DESC -> {
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(desc = intent.s)
                            )
                        }
                    }
                }

                is MviIntent.OnSignupClicked -> {
                    _state.value = _state.value.copy(
                        userInfo = UserInfo()
                    )
                    sendEvent(MviEvent.GotoSignup)
                }

                is MviIntent.CheckLogin -> {
                    sendEvent(MviEvent.GotoHome)
                }

                is MviIntent.CheckSignup -> {
                    checkSignup()
                }

                is MviIntent.OnClickProfile -> {
                    sendEvent(MviEvent.GotoProfile)
                }

                is MviIntent.CheckEditProfile -> {
                    checkEditProfile()
                }

                is MviIntent.ChangeTheme -> {
                    _state.value = _state.value.copy(darkTheme = !_state.value.darkTheme)
                }

                is MviIntent.OnClickEditProfile -> {
                    _state.value = _state.value.copy(isEdit = !_state.value.isEdit)
                }

                is MviIntent.OnChangeAvatar -> {
                    _state.value = _state.value.copy(
                        userInfo = _state.value.userInfo.copy(
                            imgUri = intent.uri
                        )
                    )
                }

                is MviIntent.LoadSong -> {
//                    if(!_state.value.isLoadSong) {
                    _state.value = _state.value.copy(
                        listSong = getAllSong(intent.context),
                        isLoadSong = true
                    )
//                    }
                }

                is MviIntent.RemoveSong -> {
//                    val songs = _state.value.selectedPlaylist!!.listSong.toMutableList().apply {
//                        removeAt(intent.index)
//                    }
//                    val playlists = _state.value.playlists.toMutableList()
//                    val index = playlists.indexOfFirst { it.id == _state.value.selectedPlaylist!!.id }
//                    _state.value = _state.value.copy(
//                        selectedPlaylist = _state.value.selectedPlaylist!!.copy(
//                            listSong = songs
//                        ),
//
//                    )
                }

                is MviIntent.OnChangeTypeListMusic -> {
                    _state.value = _state.value.copy(
                        linearListMusic = !_state.value.linearListMusic
                    )
                }

                is MviIntent.CreatePlaylist -> {
                    val oldPlaylists = _state.value.playlists
                    val newPlaylists = oldPlaylists.toMutableList().apply {
                        add(Playlist(title = intent.title))
                    }
                    _state.value = _state.value.copy(
                        playlists = newPlaylists
                    )
                }

                is MviIntent.AddSongToPlaylist -> {
                    val playlists = _state.value.playlists.toMutableList()
                    val index = playlists.indexOfFirst { it.id == intent.playlist.id }

                    if (index != -1) {
                        val playlist = playlists[index]
                        val newSongs = playlist.listSong.toMutableList()
                        newSongs.add(intent.song)
                        playlists[index] = playlist.copy(listSong = newSongs)
                        _state.value = _state.value.copy(playlists = playlists)

                    }
                }

                is MviIntent.ShowCreatePlaylistDialod -> {
                    _state.value = _state.value.copy(
                        showCreatePlaylistDialod = !_state.value.showCreatePlaylistDialod
                    )
                }

                is MviIntent.ClickItemBottomBar -> {
                    _state.value = _state.value.copy(
                        selectedBottomBar = intent.index
                    )
                }

                is MviIntent.ShowPlaylistDetail -> {
                    _state.value = _state.value.copy(
                        selectedPlaylist = intent.playlist
                    )
                    sendEvent(MviEvent.GotoPlaylistDetail)
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

    private fun checkSignup() {
        val usernameValid = isValid(_state.value.userInfo.username)
        val passValid = isValidPass(_state.value.userInfo.password)
        val pass2Valid = passValid && _state.value.userInfo.pass2 == _state.value.userInfo.password
        val emailValid = isValidEmail(_state.value.userInfo.email)
        if (usernameValid && passValid && pass2Valid && emailValid) {
            _state.value = _state.value.copy(
                userInfo = _state.value.userInfo
            )
            removeLast()
            sendEvent(MviEvent.GotoLogin)
            removeLast()
        } else {
            val inputValid = _state.value.userInfo.inputValid.copy(
                userValid = usernameValid,
                passValid = passValid,
                pass2valid = pass2Valid,
                emailValid = emailValid
            )
            _state.value = _state.value.copy(
                userInfo = _state.value.userInfo.copy(
                    inputValid = inputValid
                )
            )
        }
    }

    private suspend fun checkEditProfile() {
        val nameValid = isValid(_state.value.userInfo.name)
        val phoneValid = isValidPhone(_state.value.userInfo.phone)
        val uniValid = isValid(_state.value.userInfo.uni)
        val emailValid = isValidEmail(_state.value.userInfo.email)
        if (nameValid && phoneValid && uniValid && emailValid) {
            _state.value = _state.value.copy(
                userInfo = _state.value.userInfo,
                isEdit = !_state.value.isEdit,
                showPopup = !_state.value.showPopup
            )
            delay(2000)
            _state.value = _state.value.copy(
                showPopup = false
            )

        } else {
            val inputValid = _state.value.userInfo.inputValid.copy(
                nameValid = nameValid,
                phoneValid = phoneValid,
                uniValid = uniValid,
                emailValid = emailValid
            )
            _state.value = _state.value.copy(
                userInfo = _state.value.userInfo.copy(
                    inputValid = inputValid
                )
            )
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