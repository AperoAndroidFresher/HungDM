package com.example.hungdm.screen.player.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R

@Composable
fun PlayerControl(
    modifier: Modifier = Modifier,
    isPlay: Boolean = false,
    isShuffle: Boolean = false,
    isRepeat: Boolean = false,
    onClickPause: () -> Unit ={},
    onClickNext: () -> Unit ={},
    onClickPrevious: () -> Unit ={},
    onClickShuffle: () -> Unit = {},
    onClickRepeat: () -> Unit = {}
) {
    val icon = if (isPlay) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.size(30.dp))
        IconButton(
            onClick = onClickShuffle,
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_shuffle_24),
                contentDescription = null,
                tint = if(isShuffle) colorScheme.primary else Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onClickPrevious,
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_previous_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(
            onClick = onClickPause,
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(
            onClick = onClickNext,
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_next_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onClickRepeat,
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_repeat_24),
                contentDescription = null,
                tint = if(isRepeat) colorScheme.primary else Color.LightGray,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.size(30.dp))
    }
}