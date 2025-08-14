package com.example.hungdm.screen.playlistdetail.component

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.hungdm.R

@Composable
fun PlaylistDetailDropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(Color.DarkGray),
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.remove_song), color = Color.White) },
            onClick = {
                onClickOption1()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(painterResource(R.drawable.outline_delete_24), null, tint = Color.White)
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.share), color = Color.White) },
            onClick = {
                onClickOption2()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(painterResource(R.drawable.outline_share_24), null, tint = Color.White)
            }
        )
    }
}