package com.example.hungdm.screen.playlistdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Song
import com.example.hungdm.screen.component.SongImage
import com.example.hungdm.screen.component.SongInfo

@Composable
fun SongItemLinear(
    modifier: Modifier = Modifier,
    isPlay: Boolean = false,
    song: Song = Song(0, "Noi nay co anh", "MTP", duration = 100000L, null),
    showOption: Boolean = false,
    onClickShowOption: () -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onCLickSongPlay: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(if(isPlay) colorScheme.surfaceContainerHigh else Color.Transparent)
            .clickable {
                onCLickSongPlay()
            }
    ) {
        SongImage(img = song.img)

        Spacer(Modifier.size(8.dp))

        SongInfo(title = song.title, artist = song.artist)

        Spacer(Modifier.weight(1f))

        Text(
            text = song.time,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = colorScheme.primary,
            modifier = Modifier.padding(10.dp)
        )

        Box {
            IconButton(
                onClick = onClickShowOption,
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.about),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colorScheme.primary
                )
            }
            PlaylistDetailDropdown(
                expanded = showOption,
                onClickOption1 = onClickOption1,
                onClickOption2 = onClickOption2,
                onDismissRequest = onDismissRequest,
            )
        }
    }
}