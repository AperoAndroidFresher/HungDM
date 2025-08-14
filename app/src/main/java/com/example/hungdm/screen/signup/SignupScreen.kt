package com.example.hungdm.screen.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R
import com.example.hungdm.utils.AppUtils
import com.example.hungdm.component.Logo
import com.example.hungdm.domain.model.UserInfo
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.signup.component.SignupButton
import com.example.hungdm.screen.signup.component.SignupInput

@Composable
fun SignupScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(
            isSignup = true,
            title = stringResource(R.string.signup),
            onBack = onBack
        )
        Spacer(Modifier.size(40.dp))
        SignupInput(
            userInfo = userInfo,
            showPass = showPass,
            showPass2 = showPass2,
            onValueChangeUsername = {
                userInfo = userInfo.copy(
                    username = it,
                    inputValid = userInfo.inputValid.copy(userValid = true)
                )
            },
            onValueChangePassword = {
                userInfo = userInfo.copy(
                    password = it,
                    inputValid = userInfo.inputValid.copy(passValid = true)
                )
            },
            onValueChangePass2 = {
                userInfo = userInfo.copy(
                    pass2 = it,
                    inputValid = userInfo.inputValid.copy(pass2valid = true)
                )
            },
            onValueChangeEmail = {
                userInfo = userInfo.copy(
                    email = it,
                    inputValid = userInfo.inputValid.copy(emailValid = true)
                )
            },
            onClickShowPass = {
                showPass = !showPass
            },
            onClickShowPass2 = {
                showPass2 = !showPass2
            },
        )
        Spacer(Modifier.weight(1f))
        SignupButton(
            onClick = {
                userInfo = userInfo.copy(
                    inputValid = userInfo.inputValid.copy(
                        userValid = AppUtils.isValid(userInfo.username),
                        passValid = AppUtils.isValidPass(userInfo.password),
                        pass2valid = AppUtils.isValidPass2(userInfo.password, userInfo.pass2),
                        emailValid = AppUtils.isValidEmail(userInfo.email)
                    )
                )
                if (userInfo.inputValid.userValid && userInfo.inputValid.passValid && userInfo.inputValid.pass2valid && userInfo.inputValid.emailValid) {
                    viewModel.processIntent(MviIntent.CheckSignup(userInfo))
                }
            }
        )
    }
}