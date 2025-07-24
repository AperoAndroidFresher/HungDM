package com.example.lession6

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hungdm.R
import kotlinx.coroutines.delay

@Preview
@Composable
fun ProfilePage(modifier: Modifier = Modifier) {

    var input by remember { mutableStateOf(Input()) }
    var showPopup by rememberSaveable { mutableStateOf(false) }
    var isEdit by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(showPopup){
            PopUp(
                modifier = Modifier
                    .background(Color(0xFFFEFEFE), RoundedCornerShape(20.dp))
                    .height(350.dp)
                    .width(330.dp)
            )
            LaunchedEffect(Unit) {
                delay(2000)
                showPopup = false
            }
        }

        Title(
            isEdit = isEdit,
            title = "My Information",
            onClick = {
                isEdit = true
            }
        )

        Spacer(Modifier.size(20.dp))

        Image(
            painter = painterResource(R.drawable.img),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier= Modifier
                .clip(CircleShape)
                .size(120.dp)
        )

        Spacer(Modifier.size(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                Item(
                    modifier = Modifier.width(160.dp),
                    text = "Name".uppercase(),
                    hint = "Enter your name...",
                    value = input.name,
                    isValid = input.nameValid,
                    isEdit = isEdit,
                    onValueChange = {
                        input=input.copy(name = it)
                    }
                )

                Spacer(Modifier.weight(1f))

                Item(
                    modifier = Modifier.width(180.dp),
                    text = "Phone number".uppercase(),
                    hint = "Your phone number...",
                    value = input.phone,
                    isValid = input.phoneValid,
                    isEdit = isEdit,
                    onValueChange = {
                        input=input.copy(phone = it)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.size(20.dp))

            Item(
                modifier = Modifier.fillMaxWidth(),
                text = "University name".uppercase(),
                hint = "Your university name...",
                value = input.uni,
                isValid = input.uniValid,
                isEdit = isEdit,
                onValueChange = {
                    input=input.copy(uni = it)
                }
            )

            Spacer(Modifier.size(20.dp))

            Item(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                text = "describe yourself".uppercase(),
                hint = "Enter a description about yourself...",
                value = input.desc,
                isEdit = isEdit,
                onValueChange = {
                    input=input.copy(desc = it)
                }
            )
        }

        Spacer(Modifier.size(20.dp))

        if(isEdit){
            Button(
                onClick = {
                    val nameValid = isValid(input.name)
                    val phoneValid = isValidPhone(input.phone)
                    val uniValid = isValid(input.uni)

                    input = input.copy(
                        nameValid = nameValid,
                        phoneValid = phoneValid,
                        uniValid = uniValid
                    )

                    if (nameValid && phoneValid && uniValid) {
                        isEdit = false
                        showPopup = true
                    }
                },
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
fun Title(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    title: String ="",
    onClick: ()->Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {

        Text(
            text = title.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center)
        )

        if(!isEdit){
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier
                    .clickable { onClick() }
                    .align(Alignment.TopEnd)
                    .size(30.dp)
            )
        }
    }
}

@Composable
fun PopUp(
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.9f),
        exit = fadeOut(animationSpec = tween(200)) + scaleOut(targetScale = 0.9f)
    ){
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
        }
    }
}

@Composable
fun Item(
    modifier: Modifier = Modifier,
    text: String ="",
    value: String="",
    hint: String ="",
    isValid: Boolean = true,
    isEdit: Boolean = false,
    onValueChange: (String) -> Unit = {},
    fontSize: TextUnit = 14.sp,
    fontWeight: FontWeight = FontWeight(500),
    color: Color = Color.Gray,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {

    Column() {
        Text(
            text=text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        )

        Spacer(Modifier.size(4.dp))

        OutlinedTextField(
            modifier = modifier,
            textStyle = TextStyle(
                color= Color.Black,
                fontSize = 14.sp
            ),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = hint, fontSize = 14.sp, color = color)
            },
            keyboardOptions = keyboardOptions,
            enabled = isEdit,
        )
        Spacer(Modifier.size(4.dp))

        if(!isValid){
            Text(
                text = "Invalid format",
                color = Color.Red
            )
        }
    }
}

data class Input(
    var name: String ="",
    var phone: String ="",
    var uni: String ="",
    var desc: String ="",
    var nameValid: Boolean = true,
    var phoneValid: Boolean = true,
    var uniValid: Boolean = true
)

fun isValid(str: String): Boolean {
    val regex = Regex("^[a-zA-Z]+$")
    return regex.matches(str) && str.isNotEmpty()
}

fun isValidPhone(str: String): Boolean {
    val regex = Regex("^\\d+$")
    return regex.matches(str) && str.isNotEmpty()
}