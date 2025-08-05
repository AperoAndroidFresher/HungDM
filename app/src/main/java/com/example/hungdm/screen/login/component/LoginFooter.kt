package com.example.hungdm.screen.login.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginFooter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit ={}
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorScheme.inversePrimary)) {
                append("Don’t have an account? ")
            }
            withStyle(
                style = SpanStyle(
                    color = colorScheme.surfaceTint,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("Sign Up")
            }
        },
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White,
        modifier = modifier
            .padding(10.dp)
            .padding(bottom = 20.dp)
            .clickable {
                onClick()
            }
    )
}