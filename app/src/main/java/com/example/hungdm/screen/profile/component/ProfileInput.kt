package com.example.hungdm.screen.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hungdm.R
import com.example.hungdm.domain.model.UserInfo

@Composable
fun ProfileInput(
    modifier: Modifier = Modifier,
    userInfo: UserInfo = UserInfo(),
    isEdit: Boolean = false,
    onValueChangeName: (String) -> Unit ={},
    onValueChangePhone: (String) -> Unit ={},
    onValueChangeUni: (String) -> Unit ={},
    onValueChangeDesc: (String) -> Unit ={},
    onSubmit: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            ProfileInputItem(
                modifier = Modifier.width(160.dp),
                text = stringResource(R.string.name),
                hint = stringResource(R.string.enter_name),
                value = userInfo.name,
                isValid = userInfo.inputValid.nameValid,
                isEdit = isEdit,
                onValueChange = onValueChangeName
            )
            Spacer(Modifier.weight(1f))
            ProfileInputItem(
                modifier = Modifier.width(180.dp),
                text = stringResource(R.string.phone_number),
                hint = stringResource(R.string.enter_phone),
                value = userInfo.phone,
                isValid = userInfo.inputValid.phoneValid,
                isEdit = isEdit,
                onValueChange = onValueChangePhone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(Modifier.size(10.dp))
        ProfileInputItem(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.university),
            hint = stringResource(R.string.enter_uni),
            value = userInfo.uni,
            isValid = userInfo.inputValid.uniValid,
            isEdit = isEdit,
            onValueChange = onValueChangeUni
        )
        Spacer(Modifier.size(10.dp))
        ProfileInputItem(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            text = stringResource(R.string.desc),
            hint = stringResource(R.string.enter_desc),
            value = userInfo.desc,
            isEdit = isEdit,
            onValueChange = onValueChangeDesc
        )
        Spacer(Modifier.size(20.dp))
        if (isEdit) {
            Button(
                onClick = onSubmit,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .background(colorScheme.surfaceTint, RoundedCornerShape(10.dp))
                    .width(170.dp)
                    .height(60.dp),
            ) {
                Text(text = stringResource(R.string.submit), fontSize = 16.sp, color = Color.White)
            }
        } else {
            Button(
                onClick = onLogout,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(colorScheme.surfaceTint),
                modifier = Modifier
                    .width(170.dp)
                    .height(60.dp),
            ) {
                Text(text = stringResource(R.string.logout), fontSize = 16.sp, color = Color.Red)
            }
        }
    }
}

@Composable
fun ProfileInputItem(
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
                text = stringResource(R.string.invalid_format),
                color = Color.Red
            )
        }
    }
}