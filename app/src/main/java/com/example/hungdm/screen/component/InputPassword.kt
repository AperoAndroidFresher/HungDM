package com.example.hungdm.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun InputPassword(
    modifier: Modifier = Modifier,
    title: String = "",
    value: String = "",
    isValid: Boolean = true,
    showPass: Boolean = false,
    onValueChange: (String) -> Unit = {},
    onClickShowPass: () -> Unit = {},
) {
    val trailingIcon = if (showPass) R.drawable.outline_password_24 else R.drawable.outline_password_2_off_24
    Column {
        OutlinedTextField(
            value = value,
            leadingIcon = { Icon(painterResource(R.drawable.outline_lock_24), null, tint = colorScheme.primary) },
            onValueChange = onValueChange,
            trailingIcon = {
                Icon(
                    painter = painterResource(trailingIcon),
                    null,
                    tint = colorScheme.primary,
                    modifier = Modifier.clickable {
                        onClickShowPass()
                    }
                )
            },
            visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
            label = {
                Text(
                    title,
                    color = colorScheme.primary,
                    fontSize = 12.sp
                )
            },
            textStyle = TextStyle(
                color = colorScheme.primary,
                fontSize = 16.sp
            ),
            singleLine = true,
            modifier = modifier
                .background(colorScheme.onSecondary)
                .width(380.dp)
                .height(60.dp)
        )

        if (!isValid) {
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(R.string.invalid_format),
                color = Color.Red
            )
        }
    }
}