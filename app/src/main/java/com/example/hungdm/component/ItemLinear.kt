package com.example.hungdm.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song

@Composable
fun ItemLinear(
    modifier: Modifier = Modifier,
    option1: String = "",
    option2: String = "",
    icon1: ImageVector = Icons.Default.Delete,
    icon2: ImageVector = Icons.Default.Share,
    song: Song = Song(100, "Noi nay co anh - Son Tung MTP", "MTP", duration = 100000L, null),
    playlist: Playlist? = null,
    showOption: Boolean = false,
    onClickShowOption: () -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onCLickShowPlaylistDetail: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onCLickShowPlaylistDetail()
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(if (playlist != null) R.drawable.img1 else song.albumArtUri)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(54.dp)
        )

        Spacer(Modifier.size(8.dp))

        Column(
            modifier = Modifier
                .padding(4.dp)
                .width(210.dp)
        ) {
            Text(
                text = playlist?.title ?: song.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = playlist?.songNumberStr ?: song.artist,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

        Spacer(Modifier.weight(1f))

        if (playlist == null) {
            Text(
                text = song.time,
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = colorScheme.primary,
                modifier = Modifier.padding(10.dp)
            )
        }

        Box() {
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
            Dropdown(
                option1 = option1,
                option2 = option2,
                icon1 = icon1,
                icon2 = icon2,
                expanded = showOption,
                onClickOption1 = onClickOption1,
                onClickOption2 = onClickOption2,
                onDismissRequest = onDismissRequest,
            )
        }
    }
}