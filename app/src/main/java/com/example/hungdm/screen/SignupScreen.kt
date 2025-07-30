package com.example.hungdm.screen

import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.component.InputText
import com.example.hungdm.component.Logo
import com.example.hungdm.model.UserInfo
import com.example.hungdm.mvi.MviState


@Preview
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier,
    state: MviState = MviState(),
    onBack: () -> Unit = {},
    onSigupClick: (UserInfo) -> Unit = { },
    onValueChangeUsername: (String)->Unit={},
    onValueChangePass: (String)->Unit={},
    onValueChangePass2: (String)->Unit={},
    onValueChangeEmail: (String)->Unit={},
    checkSignup: ()->Unit = {}
) {
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
        Log.d("TAG","Back"+ state.backStack.size)
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
            onBack = onBack
        )

        Spacer(Modifier.size(40.dp))
        InputText(
            title = "Username",
            value = state.userInfo.username,
            isValid = state.userInfo.inputValid.userValid,
            onValueChange = onValueChangeUsername
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Password",
            value = state.userInfo.password,
            isValid = state.userInfo.inputValid.passValid,
            isPass = true,
            leadingIcon = Icons.Default.Lock,
            showPass = showPass,
            onValueChange = onValueChangePass,
            onClickShowPass = {
                showPass = !showPass
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Confirm password",
            value = state.userInfo.pass2,
            isValid = state.userInfo.inputValid.pass2valid,
            isPass = true,
            leadingIcon = Icons.Default.Lock,
            showPass = showPass2,
            onValueChange = onValueChangePass2,
            onClickShowPass = {
                showPass2 = !showPass2
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Email",
            value = state.userInfo.email,
            isValid = state.userInfo.inputValid.emailValid,
            leadingIcon = Icons.Default.Email,
            onValueChange = onValueChangeEmail
        )

        Spacer(Modifier.weight(1f))
        Button(
            modifier = Modifier
                .background(colorScheme.surfaceTint, RoundedCornerShape(30.dp))
                .width(380.dp)
                .height(60.dp),
            onClick = { onSigupClick(state.userInfo) }
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