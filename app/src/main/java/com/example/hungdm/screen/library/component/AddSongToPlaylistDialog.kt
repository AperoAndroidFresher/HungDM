package com.example.hungdm.screen.library.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hungdm.model.Playlist

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
                text = "Choose playlist",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            if (playlists.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "You don’t have any playlists.\n Click the '+' button to add",
                    color = Color.White
                )

                IconButton(
                    onClick = onClickNewPlaylist,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(48.dp)
                        .border(1.dp, Color.White, shape = CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(playlists.size) {
                        PlaylistItem(
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