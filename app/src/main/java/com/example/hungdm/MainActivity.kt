package com.example.hungdm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hungdm.db.AppDatabase
import com.example.hungdm.mvi.MviViewModel
import com.example.hungdm.navigation.AppNavigation
import com.example.hungdm.repo.UserRepository
import com.example.hungdm.repo.UserRepositoryImpl
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_AUDIO),
                100
            )
        }
        setContent {
            AppNavigation()
        }
    }
}


