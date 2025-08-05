package com.example.hungdm.screen.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.UtilsFunction
import com.example.hungdm.component.InputText
import com.example.hungdm.component.Logo
import com.example.hungdm.model.UserInfo
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel


@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel ,
//    onBack: () -> Unit = {},
//    onSigupClick: () -> Unit = { },
//    onValueChangeUsername: (String)->Unit={},
//    onValueChangePass: (String)->Unit={},
//    onValueChangePass2: (String)->Unit={},
//    onValueChangeEmail: (String)->Unit={},
//    checkSignup: ()->Unit = {}
) {
    var userInfo by remember { mutableStateOf(UserInfo()) }
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }

    BackHandler {
        viewModel.removeLast()
    }

    Column(
        modifier = Modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(
            isSignup = true,
            title = "Sign up",
            onBack = {
                viewModel.removeLast()
            }
        )

        Spacer(Modifier.size(40.dp))
        InputText(
            title = "Username",
            value = userInfo.username,
            isValid = userInfo.inputValid.userValid,
            onValueChange = {
                userInfo = userInfo.copy(
                    username = it,
                    inputValid = userInfo.inputValid.copy(userValid = true)
                )
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Password",
            value = userInfo.password,
            isValid = userInfo.inputValid.passValid,
            isPass = true,
            leadingIcon = Icons.Default.Lock,
            showPass = showPass,
            onValueChange = {
                userInfo = userInfo.copy(
                    password = it,
                    inputValid = userInfo.inputValid.copy(passValid = true)
                )
            },
            onClickShowPass = {
                showPass = !showPass
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Confirm password",
            value = userInfo.pass2,
            isValid = userInfo.inputValid.pass2valid,
            isPass = true,
            leadingIcon = Icons.Default.Lock,
            showPass = showPass2,
            onValueChange = {
                userInfo = userInfo.copy(
                    pass2 = it,
                    inputValid = userInfo.inputValid.copy(pass2valid = true)
                )
            },
            onClickShowPass = {
                showPass2 = !showPass2
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Email",
            value = userInfo.email,
            isValid = userInfo.inputValid.emailValid,
            leadingIcon = Icons.Default.Email,
            onValueChange = {
                userInfo = userInfo.copy(
                    email = it,
                    inputValid = userInfo.inputValid.copy(emailValid = true)
                )
            }
        )

        Spacer(Modifier.weight(1f))
        Button(
            modifier = Modifier
                .background(colorScheme.surfaceTint, RoundedCornerShape(30.dp))
                .width(380.dp)
                .height(60.dp),
            onClick = {
                userInfo = userInfo.copy(
                    inputValid = userInfo.inputValid.copy(
                        userValid = UtilsFunction.isValid(userInfo.username),
                        passValid = UtilsFunction.isValidPass(userInfo.password),
                        pass2valid = UtilsFunction.isValidPass2(userInfo.password, userInfo.pass2),
                        emailValid = UtilsFunction.isValidEmail(userInfo.email)
                    )
                )
                if (userInfo.inputValid.userValid && userInfo.inputValid.passValid && userInfo.inputValid.pass2valid && userInfo.inputValid.emailValid) {
                    viewModel.processIntent(MviIntent.CheckSignup(userInfo))
                }
            }
        ) {
            Text(
                "Sign up",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }


    }
}