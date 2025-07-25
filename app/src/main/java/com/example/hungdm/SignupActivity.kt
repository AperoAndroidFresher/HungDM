package com.example.hungdm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.hungdm.ui.theme.*

class SignupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SignupScreen()
        }
    }
}


@Preview
@Composable
fun SignupScreen(
    modifier: Modifier = Modifier
) {
    var inputInfo by remember { mutableStateOf(InputLoginSignup()) }
    var showPass by remember { mutableStateOf(false) }
    var showPass2 by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(
            isSignup = true,
            title = "Sign up",
            onBack = {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }
        )

        Spacer(Modifier.size(40.dp))
        InputText(
            title = "Username",
            value = inputInfo.user,
            isValid = inputInfo.userValid,
            onValueChange = {
                inputInfo=inputInfo.copy(user = it)
                inputInfo=inputInfo.copy(userValid = true)
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Password",
            value = inputInfo.pass,
            leadingIcon = Icons.Default.Lock,
            isValid = inputInfo.passValid,
            isPass = true,
            showPass = showPass,
            onValueChange = {
                inputInfo=inputInfo.copy(pass = it)
                inputInfo=inputInfo.copy(passValid = true)
            },
            onClickShowPass = {
                showPass=!showPass
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Confirm password",
            value = inputInfo.pass2,
            leadingIcon = Icons.Default.Lock,
            isValid = inputInfo.pass2valid,
            isPass = true,
            showPass = showPass2,
            onValueChange = {
                inputInfo=inputInfo.copy(pass2 = it)
                inputInfo=inputInfo.copy(pass2valid = true)
            },
            onClickShowPass = {
                showPass2=!showPass2
            }
        )

        Spacer(Modifier.size(10.dp))
        InputText(
            title = "Email",
            value = inputInfo.email,
            isValid = inputInfo.emailValid,
            leadingIcon = Icons.Default.Email,
            onValueChange = {
                inputInfo=inputInfo.copy(email = it)
                inputInfo=inputInfo.copy(emailValid = true)
            }
        )

        Spacer(Modifier.weight(1f))
        Button(
            modifier = Modifier
                .width(380.dp)
                .padding(bottom = 30.dp),
            onClick = {
                val userValid= isValidUser(inputInfo.user)
                val passValid= isValidPass(inputInfo.pass)
                val pass2valid= passValid&& inputInfo.pass == inputInfo.pass2
                val emailValid= isValidEmail(inputInfo.email)

                inputInfo = inputInfo.copy(
                    userValid = userValid,
                    passValid = passValid,
                    pass2valid = pass2valid,
                    emailValid = emailValid
                )

                if(userValid&&passValid&&pass2valid&&emailValid){
                    val intent = Intent(context, LoginActivity::class.java).apply {
                        putExtra("user", inputInfo.user)
                        putExtra("pass", inputInfo.pass)
                    }
                    context.startActivity(intent)

                } else{
                    if(!userValid) inputInfo=inputInfo.copy(user = "")
                    if(!passValid) inputInfo=inputInfo.copy(pass = "")
                    if(!pass2valid) inputInfo=inputInfo.copy(pass2 = "")
                    if(!emailValid) inputInfo=inputInfo.copy(email = "")
                }
            },
            colors = ButtonDefaults.buttonColors(Color(0xFF76D7E6))
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

fun noSpace(input: String): Boolean {
    return !input.contains("\\s".toRegex())
}
fun isValidUser(username: String): Boolean {
    return username.matches("^[a-zA-Z0-9]+$".toRegex()) && noSpace(username)
}
fun isValidPass(password: String): Boolean {
    return password.matches("^[a-zA-Z0-9]+$".toRegex()) && noSpace(password)
}
fun isValidEmail(email: String): Boolean {
    val regex = "^[a-zA-Z0-9._-]+@apero\\.vn$".toRegex()
    return email.matches(regex) && noSpace(email)
}
