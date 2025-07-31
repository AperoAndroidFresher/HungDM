package com.example.hungdm.mvi

import android.net.Uri
import com.example.hungdm.R
import com.example.hungdm.model.InfoName
import com.example.hungdm.model.UserInfo
import com.example.hungdm.navigation.Destination

data class MviState(
    var userInfo: UserInfo = UserInfo(),
    var backStack: MutableList<Destination> = mutableListOf(Destination.Login),
    var darkTheme: Boolean = true,
    var isEdit: Boolean = false,
    var showPopup: Boolean = false,
    var imgUri: Uri? = null
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
    data class OnChangedInput(val s: String, val infoName: InfoName) : MviIntent
}

sealed interface MviEvent{
    data object GotoHome: MviEvent
    data object GotoLogin: MviEvent
    data object GotoSignup: MviEvent
    data object GotoProfile: MviEvent
}




