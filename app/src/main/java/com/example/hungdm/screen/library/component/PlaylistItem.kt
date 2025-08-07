package com.example.hungdm.screen.library.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.screen.component.PlaylistImage
import com.example.hungdm.screen.component.PlaylistInfo

@Composable
fun PlaylistItem(
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
            songNumberStr = playlist.songNumberStr
        )
    }
}