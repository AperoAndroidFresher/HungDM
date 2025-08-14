package com.example.hungdm

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.hungdm.navigation.AppNavigation
import com.example.hungdm.utils.AppUtils
import com.example.hungdm.utils.AppUtils.getSavedLangCode

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.setLocale(this, getSavedLangCode(this))
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}

