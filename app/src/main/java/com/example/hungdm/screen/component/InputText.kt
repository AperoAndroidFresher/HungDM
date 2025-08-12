package com.example.hungdm.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun InputText(
    modifier: Modifier = Modifier,
    title: String = "",
    value: String = "",
    leadingIcon: Int = R.drawable.outline_account_circle_24,
    isValid: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    Column {
        OutlinedTextField(
            modifier = modifier
                .background(colorScheme.onSecondary)
                .width(380.dp)
                .height(60.dp),
            value = value,
            leadingIcon = { Icon(painterResource(leadingIcon), null, tint = colorScheme.primary) },
            onValueChange = onValueChange,
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
            singleLine = true
        )

        if(!isValid){
            Spacer(Modifier.size(4.dp))
            Text(
                text = stringResource(R.string.invalid_format),
                color = Color.Red
            )
        }
    }
}