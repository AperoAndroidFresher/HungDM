package com.example.hungdm.screen.login.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hungdm.component.InputPassword
import com.example.hungdm.component.InputText
import com.example.hungdm.model.UserInfo


@Composable
fun LoginInput(
    modifier: Modifier = Modifier,
    userInfo: UserInfo = UserInfo(),
    showPass: Boolean = false,
    onValueChangeUsername: (String) -> Unit = {},
    onValueChangePassword: (String) -> Unit = {},
    onClickShowPass: () -> Unit = {}
) {
    Column(
        modifier = modifier.wrapContentSize()
    ) {
        InputText(
            title = "Username",
            value = userInfo.username,
            onValueChange = onValueChangeUsername
        )
        Spacer(Modifier.size(10.dp))
        InputPassword(
            title = "Password",
            value = userInfo.password,
            showPass = showPass,
            onValueChange = onValueChangePassword,
            onClickShowPass = onClickShowPass
        )
    }
}