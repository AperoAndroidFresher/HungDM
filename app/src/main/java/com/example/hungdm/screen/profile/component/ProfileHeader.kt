package com.example.hungdm.screen.profile.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    darkTheme: Boolean = true,
    onChangeTheme: ()->Unit = {},
    onEdit: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onChangeTheme
        ) {
            Icon(
                painter = painterResource(if(darkTheme) R.drawable.light else R.drawable.dark),
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        }
        Text(
            text = "My Information",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )
        if (!isEdit) {
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onEdit
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}