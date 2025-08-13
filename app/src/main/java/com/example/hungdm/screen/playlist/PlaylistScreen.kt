package com.example.hungdm.screen.playlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.screen.mvi.MviIntent
import com.example.hungdm.screen.mvi.MviViewModel
import com.example.hungdm.screen.playlist.component.EmptyPlaylist
import com.example.hungdm.screen.playlist.component.PlaylistDialog
import com.example.hungdm.screen.playlist.component.PlaylistHeader
import com.example.hungdm.screen.playlist.component.PlaylistItem

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel,
    onBack: () -> Unit = {},
) {

    val state = viewModel.state.collectAsState()
    val playlists = state.value.playlists
    val context = LocalContext.current
    var selectedPlaylist by remember { mutableStateOf<Playlist?>(null) }
    var showCreatePlaylistDialog by remember { mutableStateOf(false) }
    var showRenamePlaylistDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if(playlists.isEmpty()){ viewModel.processIntent(MviIntent.LoadPlaylistsOfUser) }
    }

    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PlaylistHeader(
            onClickNewPlaylist = {
                showCreatePlaylistDialog = true
            }
        )

        if (playlists.isNotEmpty()) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(playlists.size) {
                    var showOption by remember { mutableStateOf(false) }
                    PlaylistItem(
                        modifier = Modifier.fillMaxWidth(),
                        playlist = playlists[it],
                        showDropDown = showOption,
                        onClickShowDropDown = {
                            showOption = true
                            selectedPlaylist = playlists[it]
                        },
                        onClickOption1 = {
                            viewModel.processIntent(MviIntent.RemovePlaylist(context, selectedPlaylist!!))
                        },
                        onClickOption2 = {
                            showRenamePlaylistDialog = true
                        },
                        onDismissRequest = {
                            showOption = false
                        },
                        onCLickShowPlaylistDetail = {
                            viewModel.processIntent(MviIntent.OnClickPlaylistDetail(playlists[it].id))
                        }
                    )
                }
            }
        } else {
            EmptyPlaylist(
                onClick = { showCreatePlaylistDialog = true }
            )
        }
    }

    if (showCreatePlaylistDialog) {
        PlaylistDialog(
            title = stringResource(R.string.new_playlist),
            confirmText = stringResource(R.string.create),
            onAction = {
                viewModel.processIntent(MviIntent.CreatePlaylist(it))
                viewModel.processIntent(MviIntent.LoadPlaylistsOfUser)
            },
            onDismissRequest = {
                showCreatePlaylistDialog = false
            }
        )
    }
    if (showRenamePlaylistDialog) {
        PlaylistDialog(
            title = stringResource(R.string.rename),
            confirmText = stringResource(R.string.rename),
            onAction = {
                viewModel.processIntent(MviIntent.RenamePlaylist(it,selectedPlaylist!!))
            },
            onDismissRequest = {
                showRenamePlaylistDialog = false
            }
        )
    }
}