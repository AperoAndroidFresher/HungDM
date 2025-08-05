package com.example.hungdm.screen.component

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R

@Composable
fun PlaylistImage(
    modifier: Modifier = Modifier,
    uri: Uri? = null
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(uri ?: R.drawable.img1)
            .crossfade(true)
            .error(R.drawable.img1)
            .size(300, 300)
            .build(),
        contentDescription = null,
        modifier = Modifier.size(54.dp)
    )
}