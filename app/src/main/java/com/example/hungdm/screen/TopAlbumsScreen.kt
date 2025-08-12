package com.example.hungdm.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.hungdm.data.remote.musicApi.dto.TopAlbums
import com.example.hungdm.screen.home.component.TopAlbumsItem

@Composable
fun TopAlbumsScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    topAlbums: TopAlbums
) {
    BackHandler { onBack() }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TopAlbumsHeader(onBack = onBack)
        Spacer(Modifier.size(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(topAlbums.album){
                TopAlbumsItem(
                    album = it
                )
            }
        }
    }
}

@Composable
fun TopAlbumsHeader(
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
            text = stringResource(R.string.top_album),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
        )
    }
}
