package com.example.hungdm.mvi

import com.example.hungdm.model.InfoName
import com.example.hungdm.model.UserInfo
import com.example.hungdm.navigation.Destination

data class MviState(
    var userInfo: UserInfo = UserInfo(),
    var backStack: MutableList<Destination> = mutableListOf(Destination.Login),
    var darkTheme: Boolean = true
)

sealed interface MviIntent{
    data class CheckSignup(val userInfo: UserInfo) : MviIntent
    data class CheckLogin(val userInfo: UserInfo) : MviIntent
    data class EditProfile(val userInfo: UserInfo) : MviIntent
    data object ChangeTheme : MviIntent
    data object OnSignupClicked : MviIntent
    data class OnChangedInput(val s: String, val infoName: InfoName) : MviIntent
    data class OnClickProfile(val userInfo: UserInfo): MviIntent
}

sealed interface MviEvent{
    data class GotoHome(val userInfo: UserInfo = UserInfo()): MviEvent
    data class GotoLogin(val userInfo: UserInfo = UserInfo()): MviEvent
    data object GotoSignup: MviEvent
    data class GotoProfile(val userInfo: UserInfo = UserInfo()): MviEvent
}




