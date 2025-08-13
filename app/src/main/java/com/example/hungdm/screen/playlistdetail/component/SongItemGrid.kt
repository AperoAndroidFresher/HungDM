package com.example.hungdm.screen.playlistdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.domain.model.Song

@Composable
fun SongItemGrid(
    modifier: Modifier = Modifier,
    isPlay: Boolean = false,
    song: Song = Song(100, "Noi nay co anh", "MTP", duration = 100000L, null),
    showOption: Boolean = false,
    onClickShowOption: () -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onCLickSongPlay: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentSize()
            .background(if(isPlay) colorScheme.surfaceContainerHigh else Color.Transparent)
            .clickable {
                onCLickSongPlay()
            }
    ) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.img)
                    .crossfade(true)
                    .error(R.drawable.img1)
                    .size(300, 300)
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = onClickShowOption,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color(0xB2000000), CircleShape)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.about),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
                PlaylistDetailDropdown(
                    expanded = showOption,
                    onClickOption1 = onClickOption1,
                    onClickOption2 = onClickOption2,
                    onDismissRequest = onDismissRequest
                )
            }
        }
        SongInfoGrid(song = song)
    }
}

@Composable
fun SongInfoGrid(
    modifier: Modifier = Modifier,
    song: Song
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = song.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
        Text(
            text = song.artist,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = song.time,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = colorScheme.primary,
            modifier = Modifier.padding(8.dp)
        )
    }
}