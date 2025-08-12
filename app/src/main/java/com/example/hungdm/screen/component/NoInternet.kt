package com.example.hungdm.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun NoInternet(
    modifier: Modifier = Modifier,
    onCLick: () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(R.drawable.outline_wifi_off_24),
            contentDescription = null,
            tint = colorScheme.surfaceTint,
            modifier = Modifier.size(90.dp)
        )
        Text(
            text = stringResource(R.string.no_internet),
            color = colorScheme.primary,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(Modifier.size(10.dp))
        Button(
            onClick = onCLick,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .background(colorScheme.surfaceTint, RoundedCornerShape(10.dp))
                .width(120.dp)
                .height(50.dp),
        ) {
            Text(text = "Try again", fontSize = 16.sp, color = Color.White)
        }
    }
}