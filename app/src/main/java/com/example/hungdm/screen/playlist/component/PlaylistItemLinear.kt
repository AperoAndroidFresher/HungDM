package com.example.hungdm.screen.playlist.component

import android.net.Uri
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
import androidx.compose.material.icons.filled.Edit
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
import com.example.hungdm.component.Dropdown
import com.example.hungdm.model.Playlist

@Composable
fun PlaylistItemLinear(
    modifier: Modifier = Modifier,
    option1: String = "",
    option2: String = "",
    icon1: ImageVector = Icons.Default.Delete,
    icon2: ImageVector = Icons.Default.Share,
    playlist: Playlist = Playlist(),
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
        PlaylistImage()

        Spacer(Modifier.size(8.dp))

        PlaylistInfo(
            title = playlist.title,
            songNumberStr = playlist.songNumberStr
        )

        Spacer(Modifier.weight(1f))

        PlaylistOptionButton(
            showOption = showOption,
            option1 = option1,
            option2 = option2,
            icon1 = icon1,
            icon2 = icon2,
            onClickShowOption = onClickShowOption,
            onClickOption1 = onClickOption1,
            onClickOption2 = onClickOption2,
            onDismissRequest = onDismissRequest
        )
    }
}

@Composable
fun PlaylistImage(
    modifier: Modifier = Modifier,
    uri: Uri? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri ?: R.drawable.img1)
            .crossfade(true)
            .error(R.drawable.img1)
            .size(300, 300)
            .build(),
        contentDescription = null,
        modifier = Modifier.size(54.dp)
    )
}

@Composable
fun PlaylistInfo(
    modifier: Modifier = Modifier,
    title: String = "playlist",
    songNumberStr: String = "0 songs"
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
            text = songNumberStr,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
fun PlaylistOptionButton(
    modifier: Modifier = Modifier,
    showOption: Boolean = false,
    option1: String = "option1",
    option2: String = "option2",
    icon1: ImageVector = Icons.Default.Delete,
    icon2: ImageVector = Icons.Default.Edit,
    onClickShowOption: () -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
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
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
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