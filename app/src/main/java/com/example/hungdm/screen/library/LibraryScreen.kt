package com.example.hungdm.screen.library

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.hungdm.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.navigation.Destination
import androidx.compose.runtime.collectAsState
import com.example.hungdm.UtilsFunction
import com.example.hungdm.screen.library.component.AddSongToPlaylistDialog
import com.example.hungdm.screen.component.SongItemLinear
import com.example.hungdm.screen.library.component.LibraryHeader

@Composable
fun LibraryScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()
    val listSong = state.value.listSong
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var showAddSongToPlaylistDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.processIntent(MviIntent.LoadSong(context))
        viewModel.processIntent(MviIntent.LoadPlaylistsOfUser)
    }

    BackHandler { onBack() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 8.dp, end = 8.dp)
    ) {
        LibraryHeader(
            onClickLocal = {},
            onClickRemote = {}
        )

        Spacer(Modifier.size(10.dp))

        if (!state.value.isLoadSong) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listSong.size) {
                    var showOption by remember { mutableStateOf(false) }
                    SongItemLinear(
                        song = listSong[it],
                        showOption = showOption,
                        option1 = "Add to playlist",
                        option2 = "Share",
                        icon1 = Icons.Default.AddCircle,
                        icon2 = Icons.Default.Share,
                        onClickShowOption = {
                            showOption = true
                            selectedSong = listSong[it]
                        },
                        onClickOption1 = {
                            showAddSongToPlaylistDialog = true
                        },
                        onClickOption2 = {
                            UtilsFunction.shareSong(context, selectedSong!!)
                        },
                        onDismissRequest = {
                            showOption = false
                        }
                    )
                }
            }

        }
    }

    if (showAddSongToPlaylistDialog) {
        AddSongToPlaylistDialog(
            playlists = state.value.playlists,
            onClickNewPlaylist = {
                showAddSongToPlaylistDialog = false
                viewModel.processIntent(MviIntent.OnClickItemBottomBar(2))
                viewModel.replace(Destination.Playlist)
            },
            onAddSongToPlaylist = {
                selectedSong?.let { selectedSong ->
                    viewModel.processIntent(MviIntent.AddSongToPlaylist(selectedSong, it))
                }
                showAddSongToPlaylistDialog = false
                selectedSong = null
            },
            onDismiss = { showAddSongToPlaylistDialog = false }
        )
    }
}