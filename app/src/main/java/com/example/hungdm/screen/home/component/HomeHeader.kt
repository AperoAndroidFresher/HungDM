package com.example.hungdm.screen.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R

@Preview
@Composable
fun HomeHeader(
    modifier: Modifier = Modifier,
    userName: String = "HungDM",
    image: Any? = R.drawable.img,
    onClickProfile: () -> Unit = {},
    onClickSetting: () -> Unit = {}
) {
    Column {
        Box(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .clickable {
                        onClickProfile()
                    }
            ) {
                UserImage(image = image)
                Spacer(Modifier.size(10.dp))
                UserInfo(userName = userName)
            }

            IconButton(
                onClick = onClickSetting,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_settings_24),
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(Modifier.size(10.dp))
        Ranking()
    }
}

@Composable
fun UserImage(
    modifier: Modifier = Modifier,
    image: Any? = R.drawable.img
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .crossfade(true)
            .placeholder(R.drawable.outline_photo_camera_24)
            .error(R.drawable.img)
            .size(300, 300)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .size(40.dp)
    )
}

@Composable
fun UserInfo(
    modifier: Modifier = Modifier,
    userName: String = "HungDM"
) {
    Column(
        modifier = modifier.width(250.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_back),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
        )
        Spacer(Modifier.size(4.dp))
        Text(
            text = userName,
            fontSize = 14.sp,
            color = colorScheme.primary,
        )
    }
}

@Composable
fun Ranking(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.ranking),
            contentDescription = null,
            tint = Color.Yellow,
            modifier = Modifier.size(30.dp)
        )
        Spacer(Modifier.size(10.dp))
        Text(
            text = stringResource(R.string.ranking),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        )
    }
}