package com.example.hungdm.screen.playlistdetail

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme.colorScheme
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
import com.example.hungdm.domain.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.playlistdetail.component.PlaylistDetailHeader
import com.example.hungdm.screen.playlistdetail.component.SongItemGrid
import com.example.hungdm.screen.playlistdetail.component.SongItemLinear

@Composable
fun PlaylistDetailScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    playlistId: Long,
    onBack: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    val playlist = state.playlists.find { it.id == playlistId }
    val songPlayIndex = if(playlist!!.id==state.playerPlaylist?.id) {
        state.playerSongIndex
    } else null
    var isLinear by remember { mutableStateOf(true) }
    var isSort by remember { mutableStateOf(false) }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(!isSort){
            PlaylistDetailHeader(
                isLinear = isLinear,
                onClick = {
                    isLinear = !isLinear
                },
                onSort = {
//                    isSort=true
//                    isLinear=true
                }
            )

            Spacer(Modifier.size(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(if(isLinear) 1 else 2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(if(isLinear) 8.dp else 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(playlist.listSong.size){
                    val isPlay = it==songPlayIndex
                    if(isLinear){
                        var showOption by remember { mutableStateOf(false) }
                        SongItemLinear(
                            isPlay = isPlay,
                            song = playlist.listSong[it],
                            showOption = showOption,
                            onClickShowOption = { showOption = true },
                            onClickOption1 = {
                                viewModel.processIntent(MviIntent.RemoveSongInPlaylist(playlist.listSong[it], playlist))
                            },
                            onClickOption2 = {},
                            onDismissRequest = { showOption = false },
                            onCLickSongPlay = {
                                viewModel.processIntent(
                                    MviIntent.OnClickPlayer(
                                        song = playlist.listSong[it],
                                        playerListSong = null,
                                        playerPlaylist = playlist
                                    )
                                )
                            }
                        )
                    } else {
                        var showOption by remember { mutableStateOf(false) }
                        SongItemGrid(
                            isPlay = isPlay,
                            song = playlist.listSong[it],
                            showOption = showOption,
                            onClickShowOption = { showOption = true },
                            onClickOption1 = {
                                viewModel.processIntent(MviIntent.RemoveSongInPlaylist(playlist.listSong[it], playlist))
                            },
                            onClickOption2 = {},
                            onDismissRequest = { showOption = false },
                            onCLickSongPlay = {
                                viewModel.processIntent(
                                    MviIntent.OnClickPlayer(
                                        song = playlist.listSong[it],
                                        playerListSong = null,
                                        playerPlaylist = playlist
                                    )
                                )
                            }
                        )
                    }
                }
            }
        } else {
//            SortHeader(
//                onBack = {
//                    isSort = false
//                }
//            )
//            Spacer(Modifier.size(10.dp))
//            SortContent(playlist = playlist)
        }
    }
}