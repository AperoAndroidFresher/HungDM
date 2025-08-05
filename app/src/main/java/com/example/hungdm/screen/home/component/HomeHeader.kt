package com.example.hungdm.screen.home.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onClick: ()->Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Home",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}