package com.example.hungdm.component

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun Option(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    option1: String = "",
    option2: String = "",
    icon1: ImageVector = Icons.Default.Delete,
    icon2: ImageVector = Icons.Default.Create
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(Color.DarkGray),
    ) {
        DropdownMenuItem(
            text = { Text(option1, color = Color.White) },
            onClick = {
                onClickOption1()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(icon1, null, tint = Color.White)
            }
        )
        DropdownMenuItem(
            text = { Text(option2, color = Color.White) },
            onClick = {
                onClickOption2()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(icon2, null, tint = Color.White)
            }
        )
    }
}