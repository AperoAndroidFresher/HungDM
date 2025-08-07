package com.example.hungdm.component

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.hungdm.R

@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    option1: String = "",
    option2: String = "",
    icon1: Int = R.drawable.outline_delete_24,
    icon2: Int = R.drawable.outline_edit_24
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
                Icon(painterResource(icon1), null, tint = Color.White)
            }
        )
        DropdownMenuItem(
            text = { Text(option2, color = Color.White) },
            onClick = {
                onClickOption2()
                onDismissRequest()
            },
            leadingIcon = {
                Icon(painterResource(icon2), null, tint = Color.White)
            }
        )
    }
}