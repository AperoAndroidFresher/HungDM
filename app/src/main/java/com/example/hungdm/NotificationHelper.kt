package com.example.hungdm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hungdm.service.AppService

class NotificationHelper(private val context: Context) {
    private val channelId = "music_channel"

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    fun createNotification(title: String, uri: Uri?, isPlaying: Boolean): Notification {
        val pauseIntent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_PAUSE
        }
        val pausePendingIntent = PendingIntent.getService(
            context, 0, pauseIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_CLOSE
        }
        val closePendingIntent = PendingIntent.getService(
            context, 1, closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(uri?.lastPathSegment ?: "Unknown")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(isPlaying)
            .addAction(android.R.drawable.ic_media_pause, "Pause", pausePendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Close", closePendingIntent)
            .build()
    }
}
