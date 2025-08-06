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
import androidx.compose.foundation.lazy.items
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hungdm.R
import com.example.hungdm.UtilsFunction
import com.example.hungdm.screen.library.component.AddSongToPlaylistDialog
import com.example.hungdm.screen.component.SongItemLinear
import com.example.hungdm.screen.library.component.LibraryHeader
import com.example.hungdm.screen.library.component.ListSongEmpty
import kotlinx.coroutines.delay

@Composable
fun LibraryScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()
    val listSongLocal = state.value.listSongLocal
    val listSongRemote = state.value.listSongRemote
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var showAddSongToPlaylistDialog by remember { mutableStateOf(false) }
    var selectedLocal by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var isLoadSong by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.processIntent(MviIntent.LoadSongLocal(context))
        viewModel.processIntent(MviIntent.LoadPlaylistsOfUser)
    }

    LaunchedEffect(key1 = isLoadSong) {
        if (isLoadSong) {
            viewModel.processIntent(MviIntent.LoadSongRemote)
            delay(2000)
            isLoadSong = false
        }
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
            selectedLocal = selectedLocal,
            onClickLocal = {
                selectedLocal = true
            },
            onClickRemote = {
                selectedLocal = false
                isLoadSong = true
            }
        )

        Spacer(Modifier.size(10.dp))

        if (isLoadSong) {
            Box(modifier = Modifier.fillMaxSize()) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_remote_item_loading))
                val progress by animateLottieCompositionAsState(composition)
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                )
            }
        } else {
            if(listSongRemote.isNotEmpty() || selectedLocal){
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(if (selectedLocal) listSongLocal else listSongRemote) {
                        var showOption by remember { mutableStateOf(false) }
                        SongItemLinear(
                            song = it,
                            showOption = showOption,
                            option1 = "Add to playlist",
                            option2 = "Share",
                            icon1 = Icons.Default.AddCircle,
                            icon2 = Icons.Default.Share,
                            onClickShowOption = {
                                showOption = true
                                selectedSong = it
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
            } else {
                ListSongEmpty(
                    onCLick = { isLoadSong = true }
                )
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