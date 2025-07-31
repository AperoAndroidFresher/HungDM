package com.example.hungdm.mvi

import android.content.Context
import android.net.Uri
import com.example.hungdm.R
import com.example.hungdm.model.InfoName
import com.example.hungdm.model.Song
import com.example.hungdm.model.UserInfo
import com.example.hungdm.navigation.Destination

data class MviState(
    var userInfo: UserInfo = UserInfo(),
    var backStack: MutableList<Destination> = mutableListOf(Destination.Login),
    var darkTheme: Boolean = true,
    var isEdit: Boolean = false,
    var showPopup: Boolean = false,
    val listSong: MutableList<Song> = mutableListOf<Song>(),
    val isLoadSong: Boolean = false,
    val linearListMusic: Boolean = true,
)

sealed interface MviIntent{
    data object OnSignupClicked : MviIntent
    data object CheckLogin : MviIntent
    data object CheckSignup : MviIntent
    data object ChangeTheme : MviIntent
    data object OnClickProfile: MviIntent
    data object OnClickEditProfile: MviIntent
    data class OnChangeAvatar(val uri: Uri): MviIntent
    data object CheckEditProfile : MviIntent
    data object OnChangeTypeListMusic: MviIntent
    data class LoadSong(val context: Context) : MviIntent
    data class RemoveSong(val index: Int, val context: Context): MviIntent
    data class OnChangedInput(val s: String, val infoName: InfoName) : MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
    data object GotoPlaylist: MviEvent
}




