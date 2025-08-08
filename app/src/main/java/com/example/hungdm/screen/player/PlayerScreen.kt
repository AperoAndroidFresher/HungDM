package com.example.hungdm.screen.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R

@Preview
@Composable
fun PlayerScreen(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(start = 20.dp, end = 20.dp),
    ){
        PlayerHeader()
        Spacer(Modifier.size(20.dp))
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.img1)
                .crossfade(true)
                .error(R.drawable.img1)
                .size(300, 300)
                .build(),
            contentDescription = null,
            modifier = Modifier.size(350.dp)
        )
        Spacer(Modifier.size(10.dp))
        Text(
            text = "Chay ngay di",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = "Son Tung",
            fontSize = 16.sp,
            color = colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
        Spacer(Modifier.size(20.dp))
        Progess()
        Spacer(Modifier.size(10.dp))
        PlayerControl()
    }
}

@Composable
fun PlayerHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onClose: ()->Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_ios_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Text(
            text = "Now playing",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_close_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun Progess(modifier: Modifier = Modifier) {
    Column {
        LinearProgressIndicator(
            progress = 0.5f,
            color = colorScheme.primary,
            trackColor = colorScheme.background,
            strokeCap = StrokeCap.Butt,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        Spacer(Modifier.size(4.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "0:50",
                color = colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Text(
                text = "3:50",
                color = colorScheme.primary,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
fun PlayerControl(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(Modifier.size(30.dp))
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_shuffle_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_previous_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_pause_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_skip_next_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_repeat_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.size(30.dp))
    }
}