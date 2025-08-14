package com.example.hungdm.screen.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.hungdm.mvi.MviViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hungdm.R
import com.example.hungdm.screen.component.NoInternet
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.screen.home.component.HomeHeader
import com.example.hungdm.screen.home.component.TopAlbums
import com.example.hungdm.screen.home.component.TopArtists
import com.example.hungdm.screen.home.component.TopTracks
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {
    val state by viewModel.state.collectAsState()
    val topAlbums = state.topAlbums
    val topTracks = state.topTracks
    val topArtists = state.topArtists
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            viewModel.processIntent(MviIntent.LoadMusicData(context))
            delay(3000)
            isLoading = false
        }
    }
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO)
        != PackageManager.PERMISSION_GRANTED
    ) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
            100
        )
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }
    }

    BackHandler { onBack() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        HomeHeader(
            userName = state.userInfo.username,
            image = state.userInfo.img,
            onClickProfile = {
                viewModel.processIntent(MviIntent.OnClickProfile)
            },
            onClickSetting = {
                viewModel.processIntent(MviIntent.OnClickSetting)
            }
        )
        Spacer(modifier = Modifier.size(10.dp))

        if (topAlbums == null || topTracks == null || topArtists == null) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_remote_item_loading))
                    val progress by animateLottieCompositionAsState(composition)
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                    )
                }
            } else {
                NoInternet(onCLick = { isLoading = true })
            }
        } else {
            TopAlbums(
                topAlbums = topAlbums,
                onClickSeeAll = { viewModel.processIntent(MviIntent.OnClickSeeAllTopAlbums) })

            TopTracks(
                topTracks = topTracks,
                onClickSeeAll = { viewModel.processIntent(MviIntent.OnClickSeeAllTopTracks) })

            TopArtists(
                topArtists = topArtists,
                onClickSeeAll = { viewModel.processIntent(MviIntent.OnClickSeeAllTopArtists) })
        }

    }
}