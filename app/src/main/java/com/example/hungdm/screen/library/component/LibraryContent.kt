package com.example.hungdm.screen.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hungdm.domain.model.Song

@Composable
fun LibraryContent(
    modifier: Modifier = Modifier,
    listSong: List<Song> = emptyList(),
    onClickShowOption: (Song) -> Unit = {},
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onCLickSongPlay: (Song) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(listSong) {
            var showDropdown by remember { mutableStateOf(false) }
            SongItemLibrary(
                song = it,
                showDropdown = showDropdown,
                onClickShowDropdown = {
                    showDropdown = true
                    onClickShowOption(it)
                },
                onClickOption1 = onClickOption1,
                onClickOption2 = onClickOption2,
                onDismissRequest = {
                    showDropdown = false
                },
                onCLickSongPlay = { onCLickSongPlay(it) }
            )
        }
    }
}