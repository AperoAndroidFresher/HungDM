package com.example.hungdm.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.hungdm.data.remote.musicApi.dto.Artist
import com.example.hungdm.data.remote.musicApi.dto.TopArtists

@Composable
fun TopArtists(
    modifier: Modifier = Modifier,
    topArtists: TopArtists,
    onClickSeeAll: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopArtistsTitle(onClickSeeAll = onClickSeeAll)
        Spacer(Modifier.size(4.dp))
        TopArtistsContent(
            topArtists = topArtists
        )
    }
}

@Composable
fun TopArtistsTitle(
    modifier: Modifier = Modifier,
    onClickSeeAll: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.top_artist),
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
fun TopArtistsContent(
    modifier: Modifier = Modifier,
    topArtists: TopArtists
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.height(200.dp)
    ) {
        items(topArtists.artist.take(5)) {
            TopArtistsItem(
                artist = it
            )
        }

    }
}

@Composable
fun TopArtistsItem(
    modifier: Modifier = Modifier,
    artist: Artist
) {
    Box(
        modifier = modifier.size(180.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(artist.image[3].url)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(180.dp)
                .align(Alignment.Center)
        )
        Text(
            text = artist.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(180.dp)
                .padding(10.dp)
        )
    }
}