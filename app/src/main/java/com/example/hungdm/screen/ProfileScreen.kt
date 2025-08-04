package com.example.hungdm.screen

import android.content.Intent
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.hungdm.UtilsFunction
import com.example.hungdm.R
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: MviViewModel
) {

    val context = LocalContext.current
    val state = viewModel.state.collectAsState()
    var userInfo by remember { mutableStateOf(state.value.userInfo) }
    var showPopup by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                userInfo = userInfo.copy(imgUri = it)
            }
        }
    )

    val focusManager = LocalFocusManager.current

    val scope = rememberCoroutineScope()


    BackHandler { viewModel.removeLast() }

    Column(
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
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
            visible = showPopup
        )

        ProfileHeader(
            title = "My Information",
            isEdit = isEdit,
            darkTheme = state.value.darkTheme,
            onChangeTheme = {
                viewModel.processIntent(MviIntent.ChangeTheme)
            },
            onEdit = { isEdit = !isEdit }
        )

        Spacer(Modifier.size(20.dp))

        Avatar(
            isEdit = isEdit,
            onChangeAvatar = {
                launcher.launch(arrayOf("image/*"))
            },
            imageUri = userInfo.imgUri ?: R.drawable.img
        )

        Spacer(Modifier.size(20.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                ProfileInput(
                    modifier = Modifier.width(160.dp),
                    text = "Name".uppercase(),
                    hint = "Enter your name...",
                    value = userInfo.name,
                    isValid = userInfo.inputValid.nameValid,
                    isEdit = isEdit,
                    onValueChange = {
                        userInfo = userInfo.copy(
                            name = it,
                            inputValid = userInfo.inputValid.copy(nameValid = true)
                        )
                    }
                )

                Spacer(Modifier.weight(1f))

                ProfileInput(
                    modifier = Modifier.width(180.dp),
                    text = "Phone number".uppercase(),
                    hint = "Your phone number...",
                    value = userInfo.phone,
                    isValid = userInfo.inputValid.phoneValid,
                    isEdit = isEdit,
                    onValueChange = {
                        userInfo = userInfo.copy(
                            phone = it,
                            inputValid = userInfo.inputValid.copy(phoneValid = true)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(Modifier.size(10.dp))

            ProfileInput(
                modifier = Modifier.fillMaxWidth(),
                text = "University name".uppercase(),
                hint = "Your university name...",
                value = userInfo.uni,
                isValid = userInfo.inputValid.uniValid,
                isEdit = isEdit,
                onValueChange = {
                    userInfo = userInfo.copy(
                        uni = it,
                        inputValid = userInfo.inputValid.copy(uniValid = true)
                    )
                }
            )

            Spacer(Modifier.size(10.dp))

            ProfileInput(
                modifier = Modifier.fillMaxWidth(),
                text = "Email".uppercase(),
                hint = "Your email...",
                value = userInfo.email,
                isValid = userInfo.inputValid.emailValid,
                isEdit = isEdit,
                onValueChange = {
                    userInfo = userInfo.copy(
                        email = it,
                        inputValid = userInfo.inputValid.copy(emailValid = true)
                    )
                }
            )

            Spacer(Modifier.size(10.dp))

            ProfileInput(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                text = "describe yourself".uppercase(),
                hint = "Enter a description about yourself...",
                value = userInfo.desc,
                isEdit = isEdit,
                onValueChange = {
                    userInfo = userInfo.copy(desc = it)
                }
            )
        }

        Spacer(Modifier.size(20.dp))

        if (isEdit) {
            Button(
                onClick = {
                    userInfo = userInfo.copy(
                        inputValid = userInfo.inputValid.copy(
                            nameValid = UtilsFunction.isValid(userInfo.name),
                            phoneValid = UtilsFunction.isValidPhone(userInfo.phone),
                            uniValid = UtilsFunction.isValid(userInfo.uni),
                            emailValid = UtilsFunction.isValidEmail(userInfo.email)
                        )
                    )
                    if (userInfo.inputValid.nameValid && userInfo.inputValid.phoneValid && userInfo.inputValid.uniValid && userInfo.inputValid.emailValid) {
                        viewModel.processIntent(MviIntent.CheckEditProfile(userInfo))
                        scope.launch{
                            showPopup = true
                            delay(1500)
                            showPopup = false
                        }
                        isEdit = false

                    }
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
fun ProfileHeader(
    modifier: Modifier = Modifier,
    title: String = "",
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
            text = title.uppercase(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
            color = colorScheme.primary
        )

        if (!isEdit) {

            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = onEdit
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = colorScheme.primary
                )
            }
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
fun ProfileInput(
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