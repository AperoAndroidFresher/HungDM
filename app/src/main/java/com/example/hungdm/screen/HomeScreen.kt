package com.example.hungdm.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.hungdm.model.UserInfo
import com.example.hungdm.mvi.MviState
import com.example.hungdm.mvi.MviViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.mvi.MviIntent

data class BottomItem(
    var label: String,
    var icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel = MviViewModel(),
    onBack: () -> Unit = {},
) {

    BackHandler { onBack() }

    Column(
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeHeader(
            onClick = {
                viewModel.processIntent(MviIntent.OnClickProfile)
            }
        )

        Spacer(modifier = Modifier.size(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.state.collectAsState().value.userInfo.username + " --- " +
                        viewModel.state.collectAsState().value.userInfo.name
            )
        }
    }
}

@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    onClick: ()->Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {

        Text(
            text = "Home",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            modifier = Modifier.align(Alignment.TopEnd),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = colorScheme.primary
            )
        }
    }
}