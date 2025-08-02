package com.example.hungdm.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.example.hungdm.component.ItemLinear
import com.example.hungdm.model.Playlist
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviState
import com.example.hungdm.mvi.MviViewModel

@Composable
fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel = MviViewModel(),
    onBack: () -> Unit = {},
) {

    val state = viewModel.state.collectAsState()
    val listSong = state.value.selectedPlaylist!!.listSong


    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(
            text = "playlist detal screen"
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(listSong.size) {
                var showOption by remember { mutableStateOf(false) }
                ItemLinear(
                    song = listSong[it],
                    showOption = showOption,
                    option1 = "Remove playlist",
                    option2 = "Share",
                    icon1 = Icons.Default.Delete,
                    icon2 = Icons.Default.Share,
                    onClickShowOption = {
                        showOption = true
                    },
                    onClickOption1 = {

                    },
                    onClickOption2 = {

                    },
                    onDismissRequest = {
                        showOption = false
                    }
                )
            }
        }
    }
}