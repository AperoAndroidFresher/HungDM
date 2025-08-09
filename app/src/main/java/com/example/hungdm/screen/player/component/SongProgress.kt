package com.example.hungdm.screen.player.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.hungdm.utils.AppUtils.formatTime
import kotlinx.coroutines.delay

@Composable
fun SongProgress(
    modifier: Modifier = Modifier,
    playerTime: Long = 0,
    duration: Long = 0,
) {
    val progress = (playerTime / duration.toFloat()).coerceIn(0f, 1f)

    Column {
        LinearProgressIndicator(
            progress = progress,
            color = colorScheme.primary,
            trackColor = colorScheme.background,
            strokeCap = StrokeCap.Butt,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Spacer(Modifier.size(4.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = formatTime(playerTime),
                color = colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Text(
                text = formatTime(duration),
                color = colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}