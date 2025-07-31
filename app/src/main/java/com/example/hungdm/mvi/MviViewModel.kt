package com.example.hungdm.mvi

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.model.InfoName
import com.example.hungdm.model.UserInfo
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

class MviViewModel : ViewModel(){
    private val _state = MutableStateFlow<MviState>(MviState())
    val state: StateFlow<MviState> = _state.asStateFlow()
    private val _event = MutableSharedFlow<MviEvent>()
    val event: SharedFlow<MviEvent> = _event.asSharedFlow()

    fun processIntent(intent: MviIntent) {
        viewModelScope.launch {
            when (intent) {
                is MviIntent.OnChangedInput -> {
                    when(intent.infoName){
                         InfoName.USERNAME->{
                            val isValid = isValid(intent.s)

                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(username = intent.s, inputValid = _state.value.userInfo.inputValid.copy(userValid = isValid))
                            )
                        }
                        InfoName.PASSWORD ->{
                            val isValid = isValidPass(intent.s)

                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(password = intent.s, inputValid = _state.value.userInfo.inputValid.copy(passValid = isValid))
                            )
                        }
                        InfoName.PASS2 ->{
                            val isValid = isValidPass(intent.s) && intent.s==_state.value.userInfo.password
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(pass2 = intent.s, inputValid = _state.value.userInfo.inputValid.copy(pass2valid = isValid))
                            )
                        }
                        InfoName.NAME ->{
                            val isValid = isValid(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(name = intent.s, inputValid = _state.value.userInfo.inputValid.copy(nameValid = isValid))
                            )
                        }
                        InfoName.PHONE ->{
                            val isValid = isValidPhone(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(phone = intent.s, inputValid = _state.value.userInfo.inputValid.copy(phoneValid = isValid))
                            )
                        }
                        InfoName.EMAIL ->{
                            val isValid = isValidEmail(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(email = intent.s, inputValid = _state.value.userInfo.inputValid.copy(emailValid = isValid))
                            )
                        }
                        InfoName.UNI ->{
                            val isValid = isValid(intent.s)
                            _state.value = _state.value.copy(
                                userInfo = _state.value.userInfo.copy(uni = intent.s, inputValid = _state.value.userInfo.inputValid.copy(uniValid = isValid))
                            )
                        }
                        InfoName.DESC ->{
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
                    val usernameValid = isValid(_state.value.userInfo.username)
                    val passValid = isValidPass(_state.value.userInfo.password)
                    val pass2Valid = passValid && _state.value.userInfo.pass2==_state.value.userInfo.password
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
                    Log.d("tag", _state.value.userInfo.toString())
                }

                is MviIntent.OnClickProfile ->{
                    sendEvent(MviEvent.GotoProfile)
                    Log.d("tag state",_state.value.userInfo.toString())
                }

                is MviIntent.CheckEditProfile -> {
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
                    Log.d("tag", _state.value.userInfo.toString())
                }

                is MviIntent.ChangeTheme -> {
                    _state.value = _state.value.copy( darkTheme = !_state.value.darkTheme)
                }

                is MviIntent.OnClickEditProfile ->{
                    _state.value = _state.value.copy( isEdit = !_state.value.isEdit)
                }

                is MviIntent.OnChangeAvatar ->{
                    _state.value = _state.value.copy(
                        imgUri = intent.uri
                    )
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

    fun replace(destination: Destination){
        val newBackStack = mutableListOf<Destination>(destination)
        _state.value = _state.value.copy(backStack = newBackStack)
    }

    private fun sendEvent(event: MviEvent){
        viewModelScope.launch {
            _event.emit(event)
        }
    }
}