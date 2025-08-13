package com.example.hungdm.screen.playlistdetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hungdm.screen.mvi.MviIntent
import com.example.hungdm.screen.mvi.MviViewModel
import com.example.hungdm.screen.playlistdetail.component.PlaylistDetailHeader
import com.example.hungdm.screen.playlistdetail.component.SongItemLinear

@Composable
fun PlaylistDetailScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    playlistId: Long,
    onBack: () -> Unit = {}
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val playlist = state.playlists.find { it.id == playlistId }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        PlaylistDetailHeader()

        Spacer(Modifier.size(10.dp))

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(playlist!!.listSong.size) {
                var showOption by remember { mutableStateOf(false) }
                SongItemLinear(
                    song = playlist.listSong[it],
                    showOption = showOption,
                    onClickShowOption = { showOption = true },
                    onClickOption1 = {
                        viewModel.processIntent(MviIntent.RemoveSongInPlaylist(context, playlist.listSong[it], playlist))
                    },
                    onClickOption2 = {},
                    onDismissRequest = { showOption = false },
                    onCLickSongPlay = {
                        viewModel.processIntent(
                            MviIntent.OnClickPlayer(
                                song = playlist.listSong[it],
                                playerListSong = null,
                                playerPlaylist = playlist,
                                context = context
                            )
                        )
                    }
                )
            }
        }
    }
}