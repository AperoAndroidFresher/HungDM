package com.example.hungdm.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun PlaylistInfo(
    modifier: Modifier = Modifier,
    title: String = "playlist",
    songNumberStr: Int = 0
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .width(210.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "$songNumberStr "+ stringResource(R.string.song_number),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}