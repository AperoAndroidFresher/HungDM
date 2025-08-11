package com.example.hungdm.screen

import android.view.Menu
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R

@Preview
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {

    var showMenu by remember { mutableStateOf(false) }

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SettingHeader(
            onBack = onBack
        )
        Spacer(Modifier.size(10.dp))
        SettingContent(
            showMenu = showMenu,
            onCLickShowMenu = { showMenu = true },
            onDismissRequest = { showMenu = false }
        )
    }
}

@Composable
fun SettingHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_arrow_back_ios_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = "Settings",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = {  },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_check_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun SettingContent(
    modifier: Modifier = Modifier,
    onCLickShowMenu: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    showMenu: Boolean
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {  },
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_language_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = "Language",
            fontSize = 20.sp,
            color = colorScheme.primary,
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = onCLickShowMenu
        ) {
            Text(
                text = "English",
                fontSize = 14.sp,
                color = colorScheme.primary,
            )
            MenuLanguage(
                expanded = showMenu,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
fun MenuLanguage(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onClickOption1: () -> Unit = {},
    onClickOption2: () -> Unit = {},
    onClickOption3: () -> Unit = {},
    onClickOption4: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(Color.DarkGray),
    ) {
        DropdownMenuItem(
            text = { Text("English", color = Color.White) },
            onClick = {
                onClickOption1()
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Korean", color = Color.White) },
            onClick = {
                onClickOption2()
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Vietnamese", color = Color.White) },
            onClick = {
                onClickOption3()
                onDismissRequest()
            }
        )
        DropdownMenuItem(
            text = { Text("Japanese", color = Color.White) },
            onClick = {
                onClickOption4()
                onDismissRequest()
            }
        )
    }
}