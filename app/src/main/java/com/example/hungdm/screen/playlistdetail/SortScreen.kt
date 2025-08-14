package com.example.hungdm.screen.playlistdetail

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import com.example.hungdm.screen.playlistdetail.component.SongItemLinear
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun SortHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_close_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = stringResource(R.string.sorting),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_check_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SortContent(
    modifier: Modifier = Modifier,
    playlist: Playlist,
) {
    val sortIndex = remember { mutableStateOf(List(playlist.listSong.size) { it }) }
    val stateSort = rememberReorderableLazyListState(onMove = { from, to ->
        sortIndex.value = sortIndex.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })

    LazyColumn(
        state = stateSort.listState,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .reorderable(stateSort)
            .detectReorderAfterLongPress(stateSort)
            .background(Color.LightGray)
    ) {
        items(sortIndex.value, { it }) { item ->
            ReorderableItem(stateSort, key = item) {
                SongItemLinear(
                    song = playlist.listSong[item]
                )
            }
        }
    }
}