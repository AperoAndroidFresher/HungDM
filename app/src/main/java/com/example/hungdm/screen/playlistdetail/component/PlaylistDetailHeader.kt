package com.example.hungdm.screen.playlistdetail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun PlaylistDetailHeader(
    modifier: Modifier = Modifier,
    isLinear: Boolean,
    onClick: ()->Unit = {},
    onSort: ()->Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(R.string.playlist_detail),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(
                onClick = onClick,
            ) {
                Icon(
                    painter = painterResource(if(isLinear) R.drawable.type else R.drawable.abou1),
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(
                onClick = onSort,
            ) {
                Icon(
                    painter = painterResource(R.drawable.sort),
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}