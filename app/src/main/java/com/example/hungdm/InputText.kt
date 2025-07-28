package com.example.hungdm

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputText(
    modifier: Modifier = Modifier,
    title: String = "",
    value: String = "",
    leadingIcon: ImageVector = Icons.Default.AccountCircle,
    isValid: Boolean = true,
    isPass: Boolean = false,
    showPass: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onClickShowPass: () -> Unit = {},
) {
    Column {
        OutlinedTextField(
            modifier = modifier
                .width(380.dp)
                .height(60.dp),
            value = value,
            leadingIcon = { Icon(leadingIcon, null, tint = Color.White) },
            onValueChange = onValueChange,
            trailingIcon = {
                if (isPass) {
                    Icon(
                        painter = painterResource(if (showPass) R.drawable.outline_password_24 else R.drawable.outline_password_2_off_24),
                        null,
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            onClickShowPass()
                        }
                    )
                }
            },
            visualTransformation = if (isPass) {
                if (showPass) VisualTransformation.None else PasswordVisualTransformation()
            } else VisualTransformation.None,
            label = {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 12.sp
                )
            },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp
            ),
            singleLine = true
        )

        if(!isValid){
            Spacer(Modifier.size(4.dp))
            Text(
                text = "Invalid format",
                color = Color.Red
            )
        }
    }
}