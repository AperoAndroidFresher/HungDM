package com.example.hungdm.screen.playlist

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.screen.mvi.MviIntent
import com.example.hungdm.screen.mvi.MviViewModel
import com.example.hungdm.screen.playlist.component.PlaylistDialog
import com.example.hungdm.screen.playlist.component.PlaylistHeader
import com.example.hungdm.screen.playlist.component.PlaylistItemLinear

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
                    PlaylistItemLinear(
                        modifier = Modifier.fillMaxWidth(),
                        playlist = playlists[it],
                        showOption = showOption,
                        option1 = "Remove playlist",
                        option2 = "Rename",
                        icon1 = R.drawable.outline_delete_24,
                        icon2 = R.drawable.outline_edit_24,
                        onClickShowOption = {
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
            title = "New playlist",
            confirmText = "Create",
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
            title = "Rename playlist",
            confirmText = "Rename",
            onAction = {
                viewModel.processIntent(MviIntent.RenamePlaylist(it,selectedPlaylist!!))
            },
            onDismissRequest = {
                showRenamePlaylistDialog = false
            }
        )
    }
}

@Composable
fun EmptyPlaylist(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "You don’t have any playlists.\n Click the '+' button to add",
            color = colorScheme.primary
        )
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .padding(top = 16.dp)
                .size(48.dp)
                .border(1.dp, colorScheme.primary, shape = CircleShape)
        ) {
            Icon(painterResource(R.drawable.outline_add_24), contentDescription = "Add", tint = colorScheme.primary)
        }
    }
}
