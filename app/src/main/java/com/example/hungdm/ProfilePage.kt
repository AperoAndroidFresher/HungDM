package com.example.lession6

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import kotlinx.coroutines.delay

@Preview
@Composable
fun ProfilePage(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {

            Text(
                text = "My Information".uppercase(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )

            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(30.dp)
            )
        }

        Spacer(Modifier.size(20.dp))

        Avatar()

        Spacer(Modifier.size(40.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                Item(
                    modifier2 = Modifier.width(160.dp),
                    text = "Name".uppercase(),
                    hint = "Enter your name...",
                )

                Spacer(Modifier.weight(1f))

                Item(
                    modifier2 = Modifier.width(180.dp),
                    text = "Phone number".uppercase(),
                    hint = "Your phone number..."
                )
            }

            Spacer(Modifier.size(20.dp))

            Item(
                modifier2 = Modifier.fillMaxWidth(),
                text = "University name".uppercase(),
                hint = "Your university name...",
            )

            Spacer(Modifier.size(20.dp))

            Item(
                modifier2 = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                text = "describe yourself".uppercase(),
                hint = "Enter a description about yourself...",
                maxLines = 5
            )


            Spacer(Modifier.size(30.dp))

            Button(
                onClick = {},
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(Color.Black),
                modifier = Modifier
                    .width(170.dp)
                    .height(60.dp)
            ) {
                Text(text="Submit", fontSize = 16.sp, color = Color.White)
            }

        }



    }
}

@Composable
fun Avatar(modifier: Modifier = Modifier) {
    Box {
        Image(
            painter = painterResource(R.drawable.img),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier= Modifier
                .clip(CircleShape)
                .size(150.dp)
        )
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomEnd),
            tint = Color(0xFF0866FF)
        )
    }
}

@Composable
fun TestPopUp(modifier: Modifier = Modifier) {
    var showPopUp by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000)
        showPopUp=false
    }


    Box(
        modifier = modifier
            .fillMaxSize()
    ){
        ProfilePage()

        if (showPopUp) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x800E0D0D))
            ){
                PopUp(
                    modifier = Modifier.align(Alignment.Center)
                        .background(Color(0xFFFEFEFE), RoundedCornerShape(20.dp))
                        .height(350.dp)
                        .width(330.dp)
                )
            }
        }

    }
}


@Composable
fun Item(
    modifier: Modifier = Modifier,
    modifier2: Modifier = Modifier,
    text: String ="",
    value: String="",
    hint: String ="",
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight(500),
    color: Color = Color.Gray,
    maxLines: Int =1
) {
    Column {
        Text(
            modifier = modifier,
            text=text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        )

        Spacer(Modifier.size(10.dp))

        OutlinedTextField(
            modifier = modifier2,
            textStyle = TextStyle(
                color= Color.Black,
                fontSize = 14.sp
            ),
            value = value,
            onValueChange = {},
            placeholder = {
                Text(text = hint, fontSize = 14.sp, color = color)
            },
            maxLines = maxLines
        )
    }
}

@Composable
fun PopUp(modifier: Modifier = Modifier) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF25AE88),
            modifier = Modifier.size(97.dp)
        )

        Text(
            text = "Success!",
            fontSize = 36.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF25AE88)
        )
        Spacer(Modifier.size(20.dp))

        Text(
            text = "Your information has \nbeen updated!",
            textAlign = TextAlign.Center,
            fontWeight = FontWeight(400),
            fontSize = 20.sp
        )

    }
}