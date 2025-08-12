package com.example.hungdm.screen.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.screen.mvi.MviIntent
import com.example.hungdm.screen.mvi.MviViewModel
import com.example.hungdm.screen.player.component.PlayerControl
import com.example.hungdm.screen.player.component.PlayerHeader
import com.example.hungdm.screen.player.component.SongProgress

@Composable
fun PlayerScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel,
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val songPLay = state.playerSong
    val playerTime = state.playerTime
    val isPlay = state.isPlay
    val context = LocalContext.current

    BackHandler { onBack() }

    Column (
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 20.dp, end = 20.dp),
    ){
        PlayerHeader(
            onBack = onBack,
            onClose = {
                onBack()
                viewModel.processIntent(MviIntent.OnClickClosePlayer(context))
            }
        )
        Spacer(Modifier.size(20.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(state.playerSong?.img)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(10.dp))
        songPLay?.title?.let {
            Text(
                text = it,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.size(4.dp))
        songPLay?.artist?.let {
            Text(
                text = it,
                fontSize = 16.sp,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        Spacer(Modifier.size(20.dp))
        songPLay?.let {
            SongProgress(
                playerTime = playerTime,
                duration = it.duration
            )
        }
        Spacer(Modifier.size(10.dp))
        PlayerControl(
            isPlay = isPlay,
            isRepeat = state.isRepeat,
            isShuffle = state.isShuffle,
            onClickPause = { viewModel.processIntent(MviIntent.OnChangeSongPlayState(context)) },
            onClickNext = { viewModel.processIntent(MviIntent.OnClickNextSong(context)) },
            onClickPrevious = { viewModel.processIntent(MviIntent.OnClickPreviousSong(context)) },
            onClickShuffle = { viewModel.processIntent(MviIntent.OnClickShuffle(context)) },
            onClickRepeat = { viewModel.processIntent(MviIntent.OnClickRepeat(context)) }
        )
    }
}