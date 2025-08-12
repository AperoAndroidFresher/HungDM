package com.example.hungdm.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Song
import com.example.hungdm.utils.AppUtils.formatTime

@Composable
fun PlayerBottomBar(
    song: Song,
    playerTime: Long = 0,
    isPlay: Boolean = false,
    onClick: () -> Unit = {},
    onCLickPause: () -> Unit = {},
    onClickClose: () -> Unit = {}
) {
    val icon = if(isPlay) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24

    val progress = playerTime.toFloat() / song.duration.toFloat()

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.background)
            .clickable {
                onClick()
            }
    ) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = colorScheme.primary,
            trackColor = colorScheme.background,
            strokeCap = StrokeCap.Butt,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCLickPause) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = colorScheme.primary
                )
            }

            Text(
                text = song.title,
                color = colorScheme.primary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.width(200.dp)
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = formatTime(playerTime),
                color = colorScheme.primary,
                fontSize = 16.sp,
            )

            IconButton(onClick = onClickClose) {
                Icon(
                    painter = painterResource(R.drawable.outline_close_24),
                    contentDescription = "Close",
                    tint = colorScheme.primary
                )
            }
        }
    }
}
