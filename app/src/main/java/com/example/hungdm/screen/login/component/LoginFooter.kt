package com.example.hungdm.screen.login.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun LoginFooter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit ={}
) {
    Text(
        text = stringResource(R.string.login_footer),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = colorScheme.primary,
        modifier = modifier
            .padding(10.dp)
            .padding(bottom = 20.dp)
            .clickable {
                onClick()
            }
    )
}