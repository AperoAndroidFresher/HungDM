package com.example.hungdm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.data.remote.musicApi.ApiMusicClient
import com.example.hungdm.data.remote.musicApi.dto.Album
import com.example.hungdm.data.remote.musicApi.dto.Artist
import com.example.hungdm.screen.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
//            TestApi()
        }
    }
}

@Composable
fun TestApi(modifier: Modifier = Modifier) {
    val list = remember { mutableStateListOf<Album>() }
    LaunchedEffect(Unit) {
        val data = ApiMusicClient.build().getTopAlbums()
        list.addAll(data.topalbums.album.toMutableList())
    }

    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ){
        items(list){
            DataItem(
                album = it
            )
        }
    }
}

@Composable
fun DataItem(modifier: Modifier = Modifier, album: Album) {
    Column {
        Text(album.name)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.image[3].url)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}