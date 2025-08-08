package com.example.hungdm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hungdm.screen.navigation.AppNavigation
import com.example.hungdm.service.AppService

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
//            MusicScreen()
        }
    }
}


//@Composable
//fun MusicScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(32.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        val context = LocalContext.current
//
//        Button(onClick = {
//            val intent = Intent(context, AppService::class.java).apply {
//                action = AppService.ACTION_PLAY
//                putExtra(AppService.EXTRA_URI, "file:///data/user/0/com.example.hungdm/files/a/Tututu%20tititu.mp3")
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                context.startForegroundService(intent)
//            } else {
//                context.startService(intent)
//            }
//        }) {
//            Text("Start")
//        }
//
//        Button(onClick = {
//            val intent = Intent(context, AppService::class.java).apply {
//                action = AppService.ACTION_PAUSE
//            }
//            context.startService(intent)
//        }) {
//            Text("Pause")
//        }
//
//        Button(onClick = {
//            val intent = Intent(context, AppService::class.java).apply {
//                action = AppService.ACTION_CLOSE
//            }
//            context.startService(intent)
//        }) {
//            Text("Destroy")
//        }
//
//    }
//}


