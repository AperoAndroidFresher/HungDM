package com.example.hungdm.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.component.ItemLinear
import com.example.hungdm.model.Playlist
import com.example.hungdm.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.navigation.Destination
import androidx.compose.runtime.collectAsState
import com.example.hungdm.UtilsFunction

@Composable
fun LibraryScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel = MviViewModel(),
    onBack: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState()
    val listSong = state.value.listSong
    var selectedSong by remember { mutableStateOf<Song?>(null) }
    var showAddSongToPlaylistDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Library",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )

        Spacer(Modifier.size(20.dp))

        Row {
            Button(
                onClick = {
//                    selectedData = 1
                },
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorScheme.surfaceTint)
            ) {
                Text(text = "Local", fontSize = 16.sp, color = Color.White)
            }

            Spacer(Modifier.size(30.dp))

            Button(
                onClick = {
//                    selectedData = 2
                },
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.DarkGray)
            ) {
                Text(text = "Remote", fontSize = 16.sp, color = Color.White)
            }
        }

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
                    ItemLinear(
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
        AddSongDialog(
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


@Composable
fun AddSongDialog(
    modifier: Modifier = Modifier,
    playlists: List<Playlist> = mutableListOf(),
    onClickNewPlaylist: () -> Unit = {},
    onAddSongToPlaylist: (Playlist) -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Choose playlist", color = Color.White, fontSize = 18.sp)

            if (playlists.isEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "You don’t have any playlists.\n Click the '+' button to add",
                    color = Color.White
                )

                IconButton(
                    onClick = onClickNewPlaylist,
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .size(48.dp)
                        .border(1.dp, Color.White, shape = CircleShape)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(playlists.size) {
                        ItemPlaylist(
                            playlist = playlists[it],
                            onAddSongToPlaylist = {
                                onAddSongToPlaylist(playlists[it])
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemPlaylist(
    modifier: Modifier = Modifier,
    playlist: Playlist = Playlist(),
    onAddSongToPlaylist: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onAddSongToPlaylist()
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.img1)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(54.dp)
        )
        Spacer(Modifier.size(8.dp))

        Column(
            modifier = Modifier
                .padding(4.dp)
                .width(210.dp)
        ) {
            Text(
                text = playlist.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = playlist.songNumberStr,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}