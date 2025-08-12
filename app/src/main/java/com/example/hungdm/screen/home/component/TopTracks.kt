package com.example.hungdm.screen.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.data.remote.musicApi.dto.TopTracks
import com.example.hungdm.data.remote.musicApi.dto.Track
import com.example.hungdm.utils.AppUtils

@Composable
fun TopTracks(
    modifier: Modifier = Modifier,
    topTracks: TopTracks = TopTracks(),
    onClickSeeAll: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        TopTracksTitle(onClickSeeAll = onClickSeeAll)
        Spacer(Modifier.size(4.dp))
        TopTracksContent(
            topTracks = topTracks
        )
    }
}

@Composable
fun TopTracksTitle(
    modifier: Modifier = Modifier,
    onClickSeeAll: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.top_track),
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
fun TopTracksContent(
    modifier: Modifier = Modifier,
    topTracks: TopTracks
) {
    val tracks = topTracks.track.take(5)
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.height(160.dp)
    ) {
        items(tracks.size) {
            TopTracksItem(
                track = tracks[it],
                color = AppUtils.color[it%8]
            )
        }

    }
}

@Composable
fun TopTracksItem(
    modifier: Modifier = Modifier,
    track: Track,
    color: Color
) {
    Box(
        modifier = modifier.size(150.dp).wrapContentSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(track.image[3].url)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .align(Alignment.Center)
        )
        Text(
            text = track.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.TopStart)
                .width(150.dp)
                .padding(10.dp)
        )
        TopTracksInfo(
            listeners = track.listeners,
            name = track.artist.name,
            color = color,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
        )
    }
}

@Composable
fun TopTracksInfo(
    modifier: Modifier = Modifier,
    listeners: String,
    name: String,
    color: Color
) {
    Column(modifier = modifier) {
        Row {
            Icon(painterResource(R.drawable.outline_hearing_24),null, tint = colorScheme.primary)
            Spacer(Modifier.size(4.dp))
            Text(
                text = listeners,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Row {
            Icon(painterResource(R.drawable.outline_artist_24),null, tint = colorScheme.primary)
            Spacer(Modifier.size(4.dp))
            Text(
                text = name,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Spacer(Modifier.height(4.dp).width(130.dp).background(color))
    }
}