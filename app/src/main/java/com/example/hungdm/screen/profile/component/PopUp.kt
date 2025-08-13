package com.example.hungdm.screen.profile.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hungdm.R

@Composable
fun PopUp(
    modifier: Modifier = Modifier,
    visible: Boolean = false
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f),
        exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 0.9f)
    ) {
        Dialog(onDismissRequest = { }) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .background(Color(0xFFFEFEFE), RoundedCornerShape(20.dp))
                    .height(350.dp)
                    .width(330.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_check_circle_24),
                        contentDescription = null,
                        tint = Color(0xFF25AE88),
                        modifier = Modifier.size(97.dp)
                    )
                    Text(
                        text = stringResource(R.string.success),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF25AE88)
                    )
                    Spacer(Modifier.size(20.dp))
                    Text(
                        text = stringResource(R.string.pop_up),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight(400),
                        fontSize = 20.sp
                    )
                }
            }
        }
    }
}