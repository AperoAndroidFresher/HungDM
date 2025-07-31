package com.example.hungdm.screen

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.R
import com.example.hungdm.model.UserInfo
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviState
import com.example.hungdm.mvi.MviViewModel
import kotlinx.coroutines.delay

@Preview
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel = MviViewModel(),
    state: MviState = MviState(),
    imageUri: Uri? = "".toUri(),
    onBack: () -> Unit = {},
    onValueChangeName: (String)->Unit={},
    onValueChangePhone: (String)->Unit={},
    onValueChangeUni: (String)->Unit={},
    onValueChangeEmail: (String)->Unit={},
    onValueChangeDesc: (String)->Unit={},
    onClickEdit: ()->Unit = {},
    onClickSubmit: ()->Unit = {}
) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.processIntent(MviIntent.OnChangeAvatar(it))
            }
        }
    )
    val focusManager = LocalFocusManager.current


    BackHandler { onBack() }

    Column(
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        PopUp(
            modifier = Modifier
                .background(Color(0xFFFEFEFE), RoundedCornerShape(20.dp))
                .height(350.dp)
                .width(330.dp),
            visible = state.showPopup
        )

        Title(
            title = "My Information",
            isEdit = state.isEdit,
            onEdit = onClickEdit
        )

        Spacer(Modifier.size(20.dp))

        Avatar(
            isEdit = state.isEdit,
            onChangeAvatar = {
                launcher.launch("image/*")
            },
            imageUri = state.userInfo.imgUri?:R.drawable.img
        )

        Spacer(Modifier.size(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                InfoText(
                    modifier = Modifier.width(160.dp),
                    text = "Name".uppercase(),
                    hint = "Enter your name...",
                    value = state.userInfo.name,
                    isValid = state.userInfo.inputValid.nameValid,
                    isEdit = state.isEdit,
                    onValueChange = onValueChangeName
                )

                Spacer(Modifier.weight(1f))

                InfoText(
                    modifier = Modifier.width(180.dp),
                    text = "Phone number".uppercase(),
                    hint = "Your phone number...",
                    value = state.userInfo.phone,
                    isValid = state.userInfo.inputValid.phoneValid,
                    isEdit = state.isEdit,
                    onValueChange = onValueChangePhone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.size(10.dp))

            InfoText(
                modifier = Modifier.fillMaxWidth(),
                text = "University name".uppercase(),
                hint = "Your university name...",
                value = state.userInfo.uni,
                isValid = state.userInfo.inputValid.uniValid,
                isEdit = state.isEdit,
                onValueChange = onValueChangeUni
            )

            Spacer(Modifier.size(10.dp))

            InfoText(
                modifier = Modifier.fillMaxWidth(),
                text = "Email".uppercase(),
                hint = "Your email...",
                value = state.userInfo.email,
                isValid = state.userInfo.inputValid.emailValid,
                isEdit = state.isEdit,
                onValueChange = onValueChangeEmail
            )

            Spacer(Modifier.size(10.dp))

            InfoText(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                text = "describe yourself".uppercase(),
                hint = "Enter a description about yourself...",
                value = state.userInfo.desc,
                isEdit = state.isEdit,
                onValueChange = onValueChangeDesc
            )
        }

        Spacer(Modifier.size(20.dp))

        if (state.isEdit) {
            Button(
                onClick = {
                    onClickSubmit()
                },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .background(colorScheme.surfaceTint, RoundedCornerShape(10.dp))
                    .width(170.dp)
                    .height(60.dp),
            ) {
                Text(text = "Submit", fontSize = 16.sp, color = Color.White)
            }
        }

    }
}

@Composable
fun Avatar(
    modifier: Modifier = Modifier,
    isEdit: Boolean = false,
    onChangeAvatar: () -> Unit = {},
    imageUri: Any = R.drawable.img
) {

    Box(
        modifier = modifier.clickable {
            if (isEdit) {
                onChangeAvatar()
            }
        }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUri)
                .crossfade(true)
                .placeholder(R.drawable.outline_photo_camera_24)
                .error(R.drawable.outline_photo_camera_24)
                .size(300, 300)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .size(120.dp)
        )
        if (isEdit) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .background(Color(0xB2000000), CircleShape)
                    .size(30.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Icon(painterResource(R.drawable.outline_photo_camera_24), null, tint = Color.White)
            }
        }
    }
}

@Composable
fun Title(
    modifier: Modifier = Modifier,
    title: String = "",
    isEdit: Boolean = false,
    onEdit: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
            color = colorScheme.primary
        )

        if (!isEdit) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier
                    .clickable { onEdit() }
                    .align(Alignment.TopEnd)
                    .size(30.dp)
            )
        }
    }
}

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
fun InfoText(
    modifier: Modifier = Modifier,
    text: String = "",
    value: String = "",
    hint: String = "",
    isValid: Boolean = true,
    isEdit: Boolean = false,
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {
    Column {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight(500),
            color = colorScheme.primary
        )


        OutlinedTextField(
            modifier = modifier.background(colorScheme.onSecondary),
            textStyle = TextStyle(
                color = colorScheme.primary,
                fontSize = 14.sp
            ),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = hint, fontSize = 14.sp, color = colorScheme.primary)
            },
            keyboardOptions = keyboardOptions,
            enabled = isEdit,
        )
        Spacer(Modifier.size(4.dp))

        if (!isValid) {
            Text(
                text = "Invalid format",
                color = Color.Red
            )
        }
    }
}

//data class Input(
//    var name: String = "",
//    var phone: String = "",
//    var uni: String = "",
//    var email: String = "",
//    var desc: String = "",
//    var nameValid: Boolean = true,
//    var phoneValid: Boolean = true,
//    var uniValid: Boolean = true
//)
//
//fun isValid(str: String): Boolean {
//    val regex = Regex("^[a-zA-Z]+$")
//    return regex.matches(str) && str.isNotEmpty()
//}
//
//fun isValidPhone(str: String): Boolean {
//    val regex = Regex("^\\d+$")
//    return regex.matches(str) && str.isNotEmpty()
//}