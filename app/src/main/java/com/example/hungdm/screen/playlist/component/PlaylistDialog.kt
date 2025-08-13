package com.example.hungdm.screen.playlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hungdm.R

@Composable
fun PlaylistDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    confirmText: String = "",
    onAction: (String) -> Unit = {},
    onDismissRequest: () -> Unit = {}
) {
    var playlistTitle by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.DarkGray, RoundedCornerShape(20.dp))
                .width(350.dp)
                .wrapContentHeight()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = playlistTitle,
                onValueChange = { playlistTitle = it },
                placeholder = { Text(stringResource(R.string.enter_name_playlist), color = Color.Gray) },
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))

            DialogActionButtons(
                confirmText = confirmText,
                onConfirm = {
                    onAction(playlistTitle)
                    onDismissRequest()
                },
                onCancel = onDismissRequest
            )
        }
    }
}

@Composable
fun DialogActionButtons(
    modifier: Modifier = Modifier,
    confirmText: String = "",
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxWidth()
    ) {
        TextButton(
            onClick = onCancel,
            modifier = Modifier.width(145.dp)
        ) {
            Text(stringResource(R.string.cancel), color = Color.White)
        }
        TextButton(
            onClick = onConfirm,
            modifier = Modifier.width(145.dp)
        ) {
            Text(confirmText, color = Color.Cyan, fontWeight = FontWeight.Bold)
        }
    }
}
