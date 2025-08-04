package com.example.hungdm.screen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.UtilsFunction
import com.example.hungdm.component.ItemLinear
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviState
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.navigation.Destination

@Composable
fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    destination: Destination.PlaylistDetail = Destination.PlaylistDetail(0),
    viewModel: MviViewModel ,
    onBack: () -> Unit = {},
) {

    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    val playlist = state.value.playlists.find { it.id == destination.playlistID }



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

        Text(
            text = "Playlist detail",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )

        Spacer(Modifier.size(20.dp))

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(playlist!!.listSong.size) {
                var showOption by remember { mutableStateOf(false) }
                ItemLinear(
                    song = playlist.listSong[it],
                    showOption = showOption,
                    option1 = "Remove from playlist",
                    option2 = "Share",
                    icon1 = Icons.Default.Delete,
                    icon2 = Icons.Default.Share,
                    onClickShowOption = {
                        showOption = true
                        Log.d("tag,", "PlaylistDetailScreen: $it")
                    },
                    onClickOption1 = {
                        viewModel.processIntent(MviIntent.RemoveSongInPlaylist(it, playlist))
                    },
                    onClickOption2 = {
                        UtilsFunction.shareSong(context, playlist.listSong[it])
                    },
                    onDismissRequest = {
                        showOption = false
                    }
                )
            }
        }
    }
}