package com.example.hungdm.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.data.remote.musicApi.dto.Album
import com.example.hungdm.data.remote.musicApi.dto.TopAlbums

@Composable
fun TopAlbums(
    modifier: Modifier = Modifier,
    topAlbums: TopAlbums = TopAlbums(),
    onClickSeeAll: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopAlbumsTitle(onClickSeeAll = onClickSeeAll)
        Spacer(Modifier.size(4.dp))
        TopAlbumsContent(
            topAlbums = topAlbums
        )
    }
}

@Composable
fun TopAlbumsTitle(
    modifier: Modifier = Modifier,
    onClickSeeAll: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.top_album),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = onClickSeeAll
        ) {
            Text(
                text = stringResource(R.string.see_all),
                fontSize = 14.sp,
                color = colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}

@Composable
fun TopAlbumsContent(
    modifier: Modifier = Modifier,
    topAlbums: TopAlbums
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.height(200.dp)
    ) {
        items(topAlbums.album.take(6)) {
            TopAlbumsItem(
                album = it
            )
        }
    }
}

@Composable
fun TopAlbumsItem(
    modifier: Modifier = Modifier,
    album: Album
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(album.image[3].url)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(54.dp)
        )
        Spacer(Modifier.size(8.dp))
        Column {
            Text(
                text = album.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = album.artist.name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }

    }
}