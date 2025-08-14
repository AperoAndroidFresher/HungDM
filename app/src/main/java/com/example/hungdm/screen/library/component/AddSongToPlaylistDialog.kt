package com.example.hungdm.screen.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hungdm.R
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.screen.component.PlaylistImage
import com.example.hungdm.screen.component.PlaylistInfo

@Composable
fun AddSongToPlaylistDialog(
    modifier: Modifier = Modifier,
    playlists: List<Playlist> = mutableListOf(),
    onClickNewPlaylist: () -> Unit = {},
    onAddSongToPlaylist: (Playlist) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp)
        ) {
            Text(
                text = stringResource(R.string.choose_playlist),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (playlists.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.empty_playlist),
                    color = Color.White
                )

                IconButton(
                    onClick = onClickNewPlaylist,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(48.dp)
                        .border(1.dp, Color.White, shape = CircleShape)
                ) {
                    Icon(painterResource(R.drawable.outline_add_24), contentDescription = null, tint = Color.White)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(playlists.size) {
                        PlaylistItemLibrary(
                            playlist = playlists[it],
                            onAddSongToPlaylist = {
                                onAddSongToPlaylist(playlists[it])
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaylistItemLibrary(
    modifier: Modifier = Modifier,
    playlist: Playlist = Playlist(),
    onAddSongToPlaylist: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onAddSongToPlaylist()
            }
    ) {
        PlaylistImage()
        Spacer(Modifier.size(8.dp))

        PlaylistInfo(
            title = playlist.title,
            songNumberStr = playlist.songNumber
        )
    }
}