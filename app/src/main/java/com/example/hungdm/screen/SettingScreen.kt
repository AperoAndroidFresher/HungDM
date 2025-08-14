package com.example.hungdm.screen

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.utils.AppUtils

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val languages = listOf(
        "en" to context.getString(R.string.english),
        "vi" to context.getString(R.string.vietnamese),
        "jp" to context.getString(R.string.japanese)
    )
    var selectedLanguage by remember { mutableStateOf(AppUtils.getSavedLangCode(context)) }

    BackHandler { onBack() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        SettingHeader(
            onBack = onBack,
            onClickOk = {
                AppUtils.setAppLanguage(selectedLanguage,context)
                onBack()
            }
        )
        Spacer(Modifier.size(10.dp))
        SettingContent(
            showMenu = showMenu,
            onCLickShowMenu = { showMenu = true },
            onDismissRequest = { showMenu = false },
            languages = languages,
            selectedLanguage = selectedLanguage,
            onLanguageSelected = {
                selectedLanguage = it
            }
        )
    }
}

@Composable
fun SettingHeader(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onClickOk: () -> Unit = {}
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
            text = stringResource(R.string.setting),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
            modifier = Modifier.align(Alignment.Center)
        )

        IconButton(
            onClick = onClickOk,
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
    languages: List<Pair<String, String>>,
    selectedLanguage: String = "",
    showMenu: Boolean,
    onLanguageSelected: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {},
        ) {
            Icon(
                painter = painterResource(R.drawable.baseline_language_24),
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = stringResource(R.string.language),
            fontSize = 20.sp,
            color = colorScheme.primary,
        )
        Spacer(Modifier.weight(1f))
        TextButton(
            onClick = onCLickShowMenu
        ) {
            Text(
                text = languages.find { it.first == selectedLanguage }?.second ?: "",
                fontSize = 14.sp,
                color = colorScheme.primary,
            )
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = onDismissRequest,
                modifier = Modifier.background(Color.DarkGray)
            ) {
                languages.forEach { (code, label) ->
                    DropdownMenuItem(
                        text = { Text(label, color = Color.White) },
                        onClick = {
                            onLanguageSelected(code)
                            onDismissRequest()
                        }
                    )
                }
            }
        }
    }
}