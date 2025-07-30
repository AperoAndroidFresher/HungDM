package com.example.hungdm.mvi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hungdm.model.InfoName
import com.example.hungdm.model.isValid
import com.example.hungdm.model.isValidEmail
import com.example.hungdm.model.isValidPass
import com.example.hungdm.model.isValidPhone
import com.example.hungdm.navigation.Destination
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
                    _event.emit(MviEvent.GotoSignup)
                }

                is MviIntent.CheckLogin -> {
                    _event.emit(MviEvent.GotoHome(intent.userInfo))
                }

                is MviIntent.CheckSignup -> {
                    val valid = intent.userInfo.inputValid.let {
                        it.userValid && it.passValid && it.emailValid && it.pass2valid
                    }
                    if (valid) {
                        _state.value = _state.value.copy(userInfo = intent.userInfo)
                        _event.emit(MviEvent.GotoLogin(intent.userInfo))
                    }
                }
                is MviIntent.OnClickProfile ->{
                    _event.emit(MviEvent.GotoProfile(intent.userInfo))
                }

                is MviIntent.EditProfile -> {
                    val valid = intent.userInfo.inputValid.let {
                        it.nameValid && it.phoneValid && it.emailValid && it.uniValid
                    }
                    if (valid) {
                        _state.value = _state.value.copy(userInfo = intent.userInfo)
                        Log.d("tag","submit ok")
                    }
                }

                is MviIntent.ChangeTheme -> {
                    _state.value = _state.value.copy( darkTheme = !_state.value.darkTheme)
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
}