package com.example.hungdm.screen.playlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.screen.component.PlaylistImage
import com.example.hungdm.screen.component.PlaylistInfo

@Composable
fun PlaylistItem(
    modifier: Modifier = Modifier,
    playlist: Playlist = Playlist(),
    isPlay: Boolean = false,
    showDropDown: Boolean = false,
    onClickShowDropDown: () -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onCLickShowPlaylistDetail: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(if(isPlay) colorScheme.surfaceContainerHigh else Color.Transparent)
            .clickable {
                onCLickShowPlaylistDetail()
            }
    ) {
        PlaylistImage()

        Spacer(Modifier.size(8.dp))

        PlaylistInfo(title = playlist.title, songNumberStr = playlist.songNumber)

        Spacer(Modifier.weight(1f))

        Box {
            IconButton(
                onClick = onClickShowDropDown,
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
            PlaylistDropdown(
                expanded = showDropDown,
                onClickOption1 = onClickOption1,
                onClickOption2 = onClickOption2,
                onDismissRequest = onDismissRequest,
            )
        }
    }
}

@Composable
fun PlaylistDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(Color.DarkGray),
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.remove_playlist), color = Color.White) },
            onClick = {
                onClickOption1()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(painterResource(R.drawable.outline_delete_24), null, tint = Color.White)
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.rename), color = Color.White) },
            onClick = {
                onClickOption2()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(painterResource(R.drawable.outline_edit_24), null, tint = Color.White)
            }
        )
    }
}