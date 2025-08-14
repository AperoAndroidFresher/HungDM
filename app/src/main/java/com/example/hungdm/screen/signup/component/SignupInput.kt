package com.example.hungdm.screen.signup.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R
import com.example.hungdm.component.InputPassword
import com.example.hungdm.component.InputText
import com.example.hungdm.domain.model.UserInfo

@Composable
fun SignupInput(
    modifier: Modifier = Modifier,
    userInfo: UserInfo = UserInfo(),
    showPass: Boolean = false,
    showPass2: Boolean = false,
    onValueChangeUsername: (String) -> Unit = {},
    onValueChangePassword: (String) -> Unit = {},
    onValueChangePass2: (String) -> Unit = {},
    onValueChangeEmail: (String) -> Unit = {},
    onClickShowPass: () -> Unit = {},
    onClickShowPass2: () -> Unit = {}
) {
    Column(
        modifier = modifier.wrapContentSize()
    ) {
        InputText(
            title = stringResource(R.string.username),
            value = userInfo.username,
            isValid = userInfo.inputValid.userValid,
            onValueChange = onValueChangeUsername
        )

        Spacer(Modifier.size(10.dp))
        InputPassword(
            title = stringResource(R.string.password),
            value = userInfo.password,
            isValid = userInfo.inputValid.passValid,
            showPass = showPass,
            onValueChange = onValueChangePassword,
            onClickShowPass = onClickShowPass
        )

        Spacer(Modifier.size(10.dp))
        InputPassword(
            title = stringResource(R.string.confirm_password),
            value = userInfo.pass2,
            isValid = userInfo.inputValid.pass2valid,
            showPass = showPass2,
            onValueChange = onValueChangePass2,
            onClickShowPass = onClickShowPass2
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = stringResource(R.string.email),
            value = userInfo.email,
            isValid = userInfo.inputValid.emailValid,
            leadingIcon = R.drawable.outline_mail_24,
            onValueChange = onValueChangeEmail
        )
    }
}