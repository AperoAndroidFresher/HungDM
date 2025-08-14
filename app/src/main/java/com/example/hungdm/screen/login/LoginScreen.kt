package com.example.hungdm.screen.login

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R
import com.example.hungdm.component.Logo
import com.example.hungdm.domain.model.UserInfo
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.login.component.LoginButton
import com.example.hungdm.screen.login.component.LoginFooter
import com.example.hungdm.screen.login.component.LoginInput
import com.example.hungdm.screen.login.component.LoginRemember

@Composable
fun LoginScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier
) {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    var showPass by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(30.dp)
    ) {
        Logo(
            title = stringResource(R.string.login_title)
        )
        Spacer(Modifier.size(40.dp))
        LoginInput(
            userInfo = userInfo,
            showPass = showPass,
            onValueChangeUsername = {
                userInfo = userInfo.copy(username = it)
            },
            onValueChangePassword = {
                userInfo = userInfo.copy(password = it)
            },
            onClickShowPass = {
                showPass = !showPass
            }
        )
        LoginRemember(
            checked = checked,
            onCheckedChange = {
                checked = !checked
            }
        )
        Spacer(Modifier.size(30.dp))
        LoginButton(
            onClick = {
                viewModel.processIntent(MviIntent.CheckLogin(userInfo, context))
            }
        )
        Spacer(Modifier.weight(1f))
        LoginFooter(
            onClick = {
                viewModel.processIntent(MviIntent.OnClickSignup)
            }
        )
    }
}