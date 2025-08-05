package com.example.hungdm.screen.home

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
import com.example.hungdm.mvi.MviViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.screen.home.component.HomeHeader

@Composable
fun HomeScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
) {

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