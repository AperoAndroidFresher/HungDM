package com.example.hungdm.screen.library.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun LibraryHeader(
    modifier: Modifier = Modifier,
    onClickLocal: () -> Unit = {},
    onClickRemote: () -> Unit = {},
) {
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Home",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary,
        )
        Spacer(Modifier.size(20.dp))

        Row {
            Button(
                onClick = onClickLocal,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorScheme.surfaceTint),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
            ) {
                Text(text = "Local", fontSize = 16.sp, color = Color.White)
            }

            Spacer(Modifier.size(30.dp))

            Button(
                onClick = onClickRemote,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(Color.DarkGray),
                modifier = Modifier
                    .width(140.dp)
                    .height(50.dp)
            ) {
                Text(text = "Remote", fontSize = 16.sp, color = Color.White)
            }
        }
    }
}