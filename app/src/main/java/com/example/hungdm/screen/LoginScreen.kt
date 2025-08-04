package com.example.hungdm.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.component.InputText
import com.example.hungdm.component.Logo
import com.example.hungdm.model.UserInfo
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel
) {

    var userInfo by remember { mutableStateOf(UserInfo()) }
    var showPass by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo()
        Spacer(Modifier.size(40.dp))

        InputText(
            title = "Username",
            value = userInfo.username,
            onValueChange = {
                userInfo = userInfo.copy(username = it)
            }
        )

        Spacer(Modifier.size(10.dp))

        InputText(
            title = "Password",
            value = userInfo.password,
            leadingIcon = Icons.Default.Lock,
            isPass = true,
            showPass = showPass,
            onValueChange = {
                userInfo = userInfo.copy(password = it)
            },
            onClickShowPass = {
                showPass=!showPass
            }
        )

        Box(
            modifier = Modifier.width(380.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = { checked = !checked},
                    colors = CheckboxDefaults.colors(checkedColor = colorScheme.surfaceTint)
                )
                Text(
                    text = "Remember me",
                    color = colorScheme.primary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.size(30.dp))

        Button(
            modifier = Modifier
                .background(colorScheme.surfaceTint, RoundedCornerShape(30.dp))
                .width(380.dp)
                .height(60.dp),
            onClick = {
                viewModel.processIntent(MviIntent.CheckLogin(userInfo))
            }
        ) {
            Text(
                "Login",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = colorScheme.inversePrimary)){
                    append("Don’t have an account? ")
                }
                withStyle(style = SpanStyle(color = colorScheme.surfaceTint, fontWeight = FontWeight.Bold)) {
                    append("Sign Up")
                }
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .padding(bottom = 20.dp)
                .clickable {
                    viewModel.processIntent(MviIntent.OnClickSignup)
                }
        )
    }
}