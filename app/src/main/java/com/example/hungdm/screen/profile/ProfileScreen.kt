package com.example.hungdm.screen.profile

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.hungdm.utils.AppUtils
import com.example.hungdm.R
import com.example.hungdm.mvi.MviIntent
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.screen.profile.component.Avatar
import com.example.hungdm.screen.profile.component.PopUp
import com.example.hungdm.screen.profile.component.ProfileHeader
import com.example.hungdm.screen.profile.component.ProfileInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: MviViewModel,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var userInfo by remember { mutableStateOf(state.userInfo) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                val inputStream = context.contentResolver.openInputStream(it)
                val byteArray = inputStream?.use { stream -> stream.readBytes() }

                byteArray?.let { byteArr ->
                    userInfo = userInfo.copy(img = byteArr)
                }
            }
        }
    )
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var showPopup by remember { mutableStateOf(false) }
    var isEdit by remember { mutableStateOf(false) }

    BackHandler { onBack() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    focusManager.clearFocus()
                }
            }
    ) {
        ProfileHeader(
            isEdit = isEdit,
            darkTheme = state.darkTheme,
            onChangeTheme = { viewModel.processIntent(MviIntent.ChangeTheme) },
            onEdit = { isEdit = !isEdit }
        )
        Spacer(Modifier.size(10.dp))
        Avatar(
            isEdit = isEdit,
            onChangeAvatar = { launcher.launch(arrayOf("image/*")) },
            image = userInfo.img ?: R.drawable.img
        )
        Spacer(Modifier.size(20.dp))
        ProfileInput(
            userInfo = userInfo,
            isEdit = isEdit,
            onValueChangeName = {
                userInfo = userInfo.copy(
                    name = it, inputValid = userInfo.inputValid.copy(nameValid = true)
                )
            },
            onValueChangePhone = {
                userInfo = userInfo.copy(
                    phone = it, inputValid = userInfo.inputValid.copy(phoneValid = true)
                )
            },
            onValueChangeUni = {
                userInfo = userInfo.copy(
                    uni = it, inputValid = userInfo.inputValid.copy(uniValid = true)
                )
            },
            onValueChangeDesc = {
                userInfo = userInfo.copy(desc = it)
            },
            onSubmit = {
                userInfo = userInfo.copy(
                    inputValid = userInfo.inputValid.copy(
                        nameValid = AppUtils.isValid(userInfo.name),
                        phoneValid = AppUtils.isValidPhone(userInfo.phone),
                        uniValid = AppUtils.isValid(userInfo.uni),
                    )
                )
                if (userInfo.inputValid.nameValid && userInfo.inputValid.phoneValid && userInfo.inputValid.uniValid) {
                    viewModel.processIntent(MviIntent.EditProfile(userInfo))
                    scope.launch{
                        showPopup = true
                        delay(1500)
                        showPopup = false
                    }
                    isEdit = false
                }
            },
            onLogout = {
                viewModel.processIntent(MviIntent.OnLogout(context))
            }
        )
        PopUp(visible = showPopup)
    }
}