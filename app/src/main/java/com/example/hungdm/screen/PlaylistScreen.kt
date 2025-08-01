package com.example.hungdm.screen

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.component.ItemGrid
import com.example.hungdm.component.ItemLinear
import com.example.hungdm.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviState
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.navigation.Destination
import java.io.File

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    state: MviState = MviState(),
    viewModel: MviViewModel = MviViewModel(),
    onBack: () -> Unit = {},
) {

    val playlists = state.playlists

    BackHandler { onBack() }

    if (state.playlists.size > 0) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(playlists.size) {
                    var showOption by remember { mutableStateOf(false) }
                    ItemLinear(
                        playlist = playlists[it],
                        showOption = showOption,
                        option1 = "Remove playlist",
                        option2 = "Rename",
                        onClickShowOption = {
                            showOption = true
                        },
                        onClickOption1 = {
                            viewModel.processIntent(MviIntent.RemoveSong(it))
                        },
                        onClickOption2 = {

                        },
                        onDismissRequest = {
                            showOption = false
                        },
                        onCLickShowPlaylistDetail = {
                            viewModel.processIntent(MviIntent.ShowPlaylistDetail(playlists[it]))
                        }
                    )
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                "You don’t have any playlists.\n Click the '+' button to add",
                color = colorScheme.primary
            )

            IconButton(
                onClick = { viewModel.processIntent(MviIntent.ShowCreatePlaylistDialod) },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .size(48.dp)
                    .border(1.dp, colorScheme.primary, shape = CircleShape)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = colorScheme.primary)
            }
        }
    }

    if (state.showCreatePlaylistDialod) {
        AddNewPlaylistDialog(
            onCreatePlaylist = {
                viewModel.processIntent(MviIntent.CreatePlaylist(it))
            },
            onDismissRequest = {
                viewModel.processIntent(MviIntent.ShowCreatePlaylistDialod)
            }
        )
    }
}


@Composable
fun AddNewPlaylistDialog(
    modifier: Modifier = Modifier,
    onCreatePlaylist: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    var playlistTitle by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(350.dp)
                .height(220.dp)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "New Playlist",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = playlistTitle,
                onValueChange = { playlistTitle = it },
                placeholder = { Text("Give your playlist a title", color = Color.Gray) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    modifier = Modifier.width(145.dp),
                    onClick = onDismissRequest
                ) {
                    Text("Cancel", color = Color.White)
                }

                TextButton(
                    modifier = Modifier.width(145.dp),
                    onClick = {
                        onCreatePlaylist(playlistTitle)
                        onDismissRequest()
                    }
                ) {
                    Text("Create", color = Color.Cyan, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}



