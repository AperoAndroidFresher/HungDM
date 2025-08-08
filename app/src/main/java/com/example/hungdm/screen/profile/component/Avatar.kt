package com.example.hungdm.screen.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    onChangeAvatar: () -> Unit = {},
    image: Any = R.drawable.img
) {
    Box(
        modifier = modifier.clickable {
            if (isEdit) {
                onChangeAvatar()
            }
        }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(image)
                .crossfade(true)
                .placeholder(R.drawable.outline_photo_camera_24)
                .error(R.drawable.outline_photo_camera_24)
                .size(300, 300)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(120.dp)
        )
        if (isEdit) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .background(Color(0xB2000000), CircleShape)
                    .size(30.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Icon(
                    painterResource(R.drawable.outline_photo_camera_24),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}