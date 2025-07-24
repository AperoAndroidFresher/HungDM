package com.example.hungdm

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun PlayListPage(modifier: Modifier = Modifier) {


    val listSong = remember { mutableStateListOf(
        Song("grainy days","moody","04:30",R.drawable.img1),
        Song("coffee","kainbeats","04:30",R.drawable.img2),
        Song("raindrops","rainyxx","00:30",R.drawable.img3),
        Song("Em cua ngay hom qua","Son Tung - MTP","04:30",R.drawable.img2),
        Song("tokyo","SmYang","02:30",R.drawable.img4),
        Song("lullaby","iamfinenow","02:30",R.drawable.img5),
        Song("Song gio","Jack - J97","04:30",R.drawable.img1),
        Song("grainy days","moody","04:30",R.drawable.img1),
        Song("coffee","kainbeats","04:30",R.drawable.img2),
        Song("raindrops","rainyxx","00:30",R.drawable.img3),
        Song("tokyo","SmYang","02:30",R.drawable.img4),
        Song("lullaby","iamfinenow","02:30",R.drawable.img5),
        Song("Song gio","Jack - J97","04:30",R.drawable.img1),
        Song("Em cua ngay hom qua","Son Tung - MTP","04:30",R.drawable.img2)
    ) }


    var linear by rememberSaveable { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            linear = linear,
            onClick = {
                linear = !linear
            }
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(if(linear) 1 else 2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(if(linear) 8.dp else 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(listSong.size){
                if(linear){
                    var showOption by remember { mutableStateOf(false) }
                    ItemLinear(
                        song = listSong[it],
                        showOption = showOption,
                        onClickShowOption = {
                            showOption = true
                        },
                        onClickRemove = {
                            listSong.removeAt(it)
                            showOption = false
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
                            listSong.removeAt(it)
                            showOption = false
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
    title: String ="My Playlist",
    onClick: ()->Unit = {}
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
                    painter = if(linear) painterResource(R.drawable.type) else painterResource(R.drawable.type1),
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



@Composable
fun ItemLinear(
    modifier: Modifier = Modifier,
    song:Song = Song("","","",R.drawable.img1),
    showOption: Boolean = false,
    onClickShowOption: () -> Unit={},
    onClickRemove: () -> Unit={},
    onDismissRequest: () -> Unit={}
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painterResource(song.img),
            contentDescription = null,
            modifier = Modifier.size(54.dp)
        )
        Spacer(Modifier.size(8.dp))

        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Text(
                text = song.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.author,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Spacer(Modifier.weight(1f))
        Text(
            text = song.time,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = Color.White,
            modifier = Modifier.padding(10.dp)
        )

        Box(){
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
                onDismissRequest = onDismissRequest,
            )
        }

    }
}


//@Preview
@Composable
fun ItemGrid(
    modifier: Modifier = Modifier,
    song:Song = Song("Song gio","J97","04:30",R.drawable.img1),
    showOption: Boolean = false,
    onClickShowOption: () -> Unit={},
    onClickRemove: () -> Unit={},
    onDismissRequest: () -> Unit={}
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.wrapContentSize()
    ) {
        Box() {
            Image(
                painter = painterResource(song.img),
                contentDescription = null,
                modifier = Modifier
                    .size(135.dp)
                    .align(Alignment.Center)
            )

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ){
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
            text = song.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = song.author,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Text(
            text = song.time,
            fontSize = 16.sp,
            fontWeight = FontWeight(400),
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )

    }


}

@Composable
fun Option(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickRemove: ()->Unit = {},
    onDismissRequest: ()->Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest =onDismissRequest,
        modifier = modifier.background(Color.DarkGray),
    ) {
        DropdownMenuItem(
            text = { Text("Remove from playlist", color = Color.White) },
            onClick = onClickRemove,
            leadingIcon = {
                Icon(Icons.Default.Delete,null, tint = Color.White)
            }
        )
        DropdownMenuItem(
            text = { Text("Share (coming soon)", color = Color(0x60FFFFFF)) },
            onClick = onDismissRequest,
            leadingIcon = {
                Icon(Icons.Default.Share,null, tint = Color.White)
            }
        )
    }
}


data class Song(
    var name: String="",
    var author: String="",
    var time: String="",
    var img: Int = 0,
)