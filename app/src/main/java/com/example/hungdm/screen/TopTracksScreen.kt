package com.example.hungdm.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.data.remote.musicApi.dto.TopTracks
import com.example.hungdm.screen.home.component.TopTracksItem
import com.example.hungdm.utils.AppUtils

@Composable
fun TopTracksScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    topTracks: TopTracks,
) {
    BackHandler { onBack() }

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        TopTracksHeader(onBack = onBack)
        Spacer(Modifier.size(10.dp))
        LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(30.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            items(topTracks.track.size){
                TopTracksItem(
                    track = topTracks.track[it],
                    color = AppUtils.color[it%8]
                )
            }
        }
    }
}

@Composable
fun TopTracksHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onBack,
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_ios_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.top_track),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
        )
    }
}