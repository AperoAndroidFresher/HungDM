package com.example.hungdm.screen.library


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.example.hungdm.domain.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import androidx.compose.runtime.collectAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hungdm.R
import com.example.hungdm.screen.component.NoInternet
import com.example.hungdm.utils.AppUtils
import com.example.hungdm.screen.library.component.AddSongToPlaylistDialog
import com.example.hungdm.screen.library.component.LibraryContent
import com.example.hungdm.screen.library.component.LibraryHeader
import kotlinx.coroutines.delay

@Composable
fun LibraryScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onClickNewPlaylist: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var isLoadSong by remember { mutableStateOf(true) }
    var showAddSongToPlaylistDialog by remember { mutableStateOf(false) }
    var selectedLocal by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if(state.listSongLocal.isEmpty()) { viewModel.processIntent(MviIntent.LoadSongLocal(context)) }
        viewModel.processIntent(MviIntent.LoadPlaylistsOfUser)
    }

    LaunchedEffect(key1 = isLoadSong) {
        if (isLoadSong) {
            viewModel.processIntent(MviIntent.LoadSongRemote(context))
            delay(1000)
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
            if (selectedLocal) {
                LibraryContent(
                    listSong = state.listSongLocal,
                    onClickShowOption = {
                        selectedSong = it
                    },
                    onClickOption1 = {
                        showAddSongToPlaylistDialog = true
                    },
                    onClickOption2 = {
                        AppUtils.shareSong(context, selectedSong!!)
                    },
                    onCLickSongPlay = {
                        viewModel.processIntent(
                            MviIntent.OnClickPlayer(
                                song = it,
                                playerListSong = state.listSongLocal,
                                playerPlaylist = null
                            )
                        )
                    }
                )
            } else {
                if (state.listSongRemote.isNotEmpty()) {
                    LibraryContent(
                        listSong = state.listSongRemote,
                        onClickShowOption = {
                            selectedSong = it
                        },
                        onClickOption1 = {
                            showAddSongToPlaylistDialog = true
                        },
                        onClickOption2 = {
                            // AppUtils.shareSong(context, selectedSong!!)
                        },
                        onCLickSongPlay = {
                            viewModel.processIntent(
                                MviIntent.OnClickPlayer(
                                    song = it,
                                    playerListSong = state.listSongRemote,
                                    playerPlaylist = null
                                )
                            )
                        }
                    )
                } else {
                    NoInternet(
                        onCLick = { isLoadSong = true }
                    )
                }
            }

        }
    }

    if (showAddSongToPlaylistDialog) {
        AddSongToPlaylistDialog(
            playlists = state.playlists,
            onClickNewPlaylist = {
                showAddSongToPlaylistDialog = false
                onClickNewPlaylist()
            },
            onAddSongToPlaylist = {
                viewModel.processIntent(MviIntent.AddSongToPlaylist(selectedSong!!, it))
                showAddSongToPlaylistDialog = false
                selectedSong = null
            },
            onDismiss = { showAddSongToPlaylistDialog = false }
        )
    }
}