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
import com.example.hungdm.screen.mvi.MviIntent
import com.example.hungdm.screen.mvi.MviViewModel
import androidx.compose.runtime.collectAsState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hungdm.R
import com.example.hungdm.screen.component.NoInternet
import com.example.hungdm.utils.AppUtils
import com.example.hungdm.screen.library.component.AddSongToPlaylistDialog
import com.example.hungdm.screen.component.SongItemLinear
import com.example.hungdm.screen.library.component.LibraryHeader
import kotlinx.coroutines.delay

@Composable
fun LibraryScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onClickNewPlaylist: () -> Unit = {},
    onBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()
    val listSongLocal = state.value.listSongLocal
    val listSongRemote = state.value.listSongRemote
    val context = LocalContext.current
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var isLoadSong by remember { mutableStateOf(true) }
    var showAddSongToPlaylistDialog by remember { mutableStateOf(false) }
    var selectedLocal by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if(listSongLocal.isEmpty()) { viewModel.processIntent(MviIntent.LoadSongLocal(context)) }
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
                    listSong = listSongLocal,
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
                                playerListSong = listSongLocal,
                                playerPlaylist = null,
                                context = context
                            )
                        )
                    }
                )
            } else {
                if (listSongRemote.isNotEmpty()) {
                    LibraryContent(
                        listSong = listSongRemote,
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
                                    playerListSong = listSongRemote,
                                    playerPlaylist = null,
                                    context = context
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
            playlists = state.value.playlists,
            onClickNewPlaylist = {
                showAddSongToPlaylistDialog = false
                onClickNewPlaylist()
            },
            onAddSongToPlaylist = {
                viewModel.processIntent(MviIntent.AddSongToPlaylist(context, selectedSong!!, it))
                showAddSongToPlaylistDialog = false
                selectedSong = null
            },
            onDismiss = { showAddSongToPlaylistDialog = false }
        )
    }
}

@Composable
fun LibraryContent(
    modifier: Modifier = Modifier,
    listSong: List<Song> = emptyList(),
    onClickShowOption: (Song) -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onCLickSongPlay: (Song) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listSong) {
            var showOption by remember { mutableStateOf(false) }
            SongItemLinear(
                song = it,
                showOption = showOption,
                option1 = "Add to playlist",
                option2 = "Share",
                icon1 = R.drawable.outline_add_24,
                icon2 = R.drawable.outline_share_24,
                onClickShowOption = {
                    showOption = true
                    onClickShowOption(it)
                },
                onClickOption1 = onClickOption1,
                onClickOption2 = onClickOption2,
                onDismissRequest = {
                    showOption = false
                },
                onCLickSongPlay = { onCLickSongPlay(it) }
            )
        }
    }
}