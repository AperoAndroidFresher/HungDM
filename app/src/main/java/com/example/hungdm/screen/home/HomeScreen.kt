package com.example.hungdm.screen.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hungdm.screen.mvi.MviViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hungdm.screen.mvi.MviIntent
import com.example.hungdm.screen.home.component.HomeHeader

@Composable
fun HomeScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {
    val context = LocalContext.current

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
            100
        )
    }

    BackHandler { onBack() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp),
    ) {
        HomeHeader(
            onClick = {
                viewModel.processIntent(MviIntent.OnClickProfile)
            }
        )

        Spacer(modifier = Modifier.size(20.dp))

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = viewModel.state.collectAsState().value.userInfo.username + " --- " +
                        viewModel.state.collectAsState().value.userInfo.name
            )
        }
    }
}