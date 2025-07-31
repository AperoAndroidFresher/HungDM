package com.example.hungdm.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.model.Song
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviState
import com.example.hungdm.mvi.MviViewModel
import java.io.File

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    state: MviState= MviState(),
    viewModel: MviViewModel = MviViewModel(),
    onBack: () -> Unit = {},
    onClickRemove: (Int) -> Unit = {}
) {

    val listSong = state.listSong
    val context = LocalContext.current


    BackHandler { onBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (state.linearListMusic) 1 else 2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(if (state.linearListMusic) 8.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(listSong.size) {
                if (state.linearListMusic) {
                    var showOption by remember { mutableStateOf(false) }
                    ItemLinear(
                        song = listSong[it],
                        showOption = showOption,
                        onClickShowOption = {
                            showOption = true
                            Log.d("tag", "showOption "+ state.listSong.size.toString())
                            Log.d("tag", "showOption2 "+ viewModel.state.value.listSong.size.toString())
                        },
                        onClickRemove = {
//                            listSong.removeAt(it)
//                            showOption = false
                            viewModel.processIntent(MviIntent.RemoveSong(it,context))
                        },
                        onDismissRequest = {
                            showOption = false
                        }
                    )
                } else {
                    var showOption by remember { mutableStateOf(false) }
                    ItemGrid(
                        song = listSong[it],
                        showOption = showOption,
                        onClickShowOption = {
                            showOption = true
                        },
                        onClickRemove = {
//                            listSong.removeAt(it)
//                            showOption = false
                            viewModel.processIntent(MviIntent.RemoveSong(it,context))
                        },
                        onDismissRequest = {
                            showOption = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    linear: Boolean = true,
    title: String = "My Playlist",
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
            color = Color.White,
        )

        Row(
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    painter = if (linear) painterResource(R.drawable.type) else painterResource(R.drawable.type1),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemLinear(
    modifier: Modifier = Modifier,
    song: Song = Song(100,"Noi nay co anh - Son Tung MTP","MTP", duration = 100000L,null),
    showOption: Boolean = false,
    onClickShowOption: () -> Unit = {},
    onClickRemove: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(song.albumArtUri)
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
                text = song.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artist,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = song.time,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = colorScheme.primary,
            modifier = Modifier.padding(10.dp)
        )

        Box() {
            IconButton(
                onClick = onClickShowOption,
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.about),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colorScheme.primary
                )
            }
            Option(
                expanded = showOption,
                onClickRemove = onClickRemove,
                onDismissRequest = onDismissRequest,
            )
        }

    }
}


@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    song: Song = Song(100,"Noi nay co anh - Son Tung MTP","MTP", duration = 100000L ,null),
    showOption: Boolean = false,
    onClickShowOption: () -> Unit = {},
    onClickRemove: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentSize()
    ) {
        Box() {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(song.albumArtUri)
                    .crossfade(true)
                    .error(R.drawable.img1)
                    .size(300, 300)
                    .build(),
                contentDescription = null,
                modifier = Modifier.size(135.dp)
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                IconButton(
                    onClick = onClickShowOption,
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color(0xB2000000), CircleShape)
                        .size(30.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.about),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
                Option(
                    expanded = showOption,
                    onClickRemove = onClickRemove,
                    onDismissRequest = onDismissRequest
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
            text = song.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = song.artist,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = song.time,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = colorScheme.primary,
            modifier = Modifier.padding(8.dp)
        )

    }


}

@Composable
fun Option(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickRemove: () -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(Color.DarkGray),
    ) {
        DropdownMenuItem(
            text = { Text("Remove from playlist", color = Color.White) },
            onClick = {
                onClickRemove()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(Icons.Default.Delete, null, tint = Color.White)
            }
        )
        DropdownMenuItem(
            text = { Text("Share (coming soon)", color = Color(0x60FFFFFF)) },
            onClick = onDismissRequest,
            leadingIcon = {
                Icon(Icons.Default.Share, null, tint = Color.White)
            }
        )
    }
}
