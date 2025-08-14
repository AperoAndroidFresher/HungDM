package com.example.hungdm.screen.playlistdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.domain.model.Song
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.playlistdetail.component.SongItemLinear
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun SortScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val fechdata = listOf(
        Song(0,"Noi nay co anh","Sontung MTP",100),
        Song(0,"Noi nay co anh","Sontung MTP",100),
        Song(0,"Noi nay co anh","Sontung MTP",100),
        Song(0,"Noi nay co anh","Sontung MTP",100)
    )
    val sortIndex = remember { mutableStateOf(List(5) { it }) }
    val stateSort = rememberReorderableLazyListState(onMove = { from, to ->
        sortIndex.value = sortIndex.value.toMutableList().apply {
            add(to.index, removeAt(from.index))
        }
    })


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SortHeader()

        Spacer(Modifier.size(10.dp))

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
                        song = fechdata[item]
                    )
                }
            }
        }

    }
}

@Composable
fun SortHeader(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = {},
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_close_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = stringResource(R.string.playlist_detail),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = {},
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

//@Composable
//fun DropAndDrag(modifier: Modifier = Modifier) {
//
//    val fechdata = listOf(
//        Song(0,"Noi nay co anh","Sontung MTP",100),
//        Song(0,"Em cua ngay hom qua","Sontung MTP",100),
//        Song(0,"Lac troi","Sontung MTP",100),
//        Song(0,"Nang am xa dan","Sontung MTP",100)
//    )
//    val data = remember { mutableStateOf(List(5) { it }) }
//    val state = rememberReorderableLazyListState(onMove = { from, to ->
//        data.value = data.value.toMutableList().apply {
//            add(to.index, removeAt(from.index))
//        }
//    })
//    Column {
//        Song(0,"Hay trao cho anh","Sontung MTP",100),
//        Spacer(Modifier.size(50.dp))
//        Text(
//            "-------------------------------"
//        )
//        LazyColumn(
//            state = state.listState,
//            verticalArrangement = Arrangement.spacedBy(10.dp),
//            modifier = Modifier
//                .reorderable(state)
//                .detectReorderAfterLongPress(state)
//                .background(Color.LightGray)
//        ) {
//            items(data.value, { it }) { item ->
//                ReorderableItem(state, key = item) {
////                    Column(
////                        modifier = Modifier
////                            .fillMaxWidth()
////                            .height(60.dp)
////                            .background(Color.Gray)
////                    ) {
//                    SongItemLinear(
//                        song = fechdata[item]
//                    )
////                    }
//                }
            }
        }
    }

}