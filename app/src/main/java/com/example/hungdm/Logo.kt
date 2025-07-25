package com.example.hungdm

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Logo(
    modifier: Modifier = Modifier,
    isSignup: Boolean = false,
    onBack: ()->Unit = {},
    title: String = "Login to your account",
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        if(isSignup){
            Icon(
                Icons.Default.KeyboardArrowLeft,
                null,
                tint = Color.White,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        onBack()
                    }
            )
        }
        Image(
            painter = painterResource(R.drawable.logoapp),
            contentDescription = null,
            modifier = Modifier
                .width(280.dp)
                .height(250.dp)
                .align(Alignment.Center)
        )
        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}