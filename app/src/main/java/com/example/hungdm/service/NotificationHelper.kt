package com.example.hungdm.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.hungdm.MainActivity
import com.example.hungdm.R

class NotificationHelper(private val context: Context) {
    private val channelId = "music_channel"
    private val notificationId = 1

    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


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


    fun createNotification(songTitle: String, isPlaying: Boolean): Notification {
        return buildNotification(songTitle,isPlaying)
    }

    fun updateNotification(songTitle: String, isPlaying: Boolean) {
        val notification = buildNotification(songTitle, isPlaying)
        notificationManager.notify(notificationId, notification)
    }

    private fun buildNotification(songTitle: String, isPlaying: Boolean): Notification {
        val prevIntent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_PREVIOUS
        }
        val prevPending = PendingIntent.getService(
            context, 0, prevIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val pauseResumeIntent = Intent(context, AppService::class.java).apply {
            action = if (isPlaying) AppService.ACTION_PAUSE else AppService.ACTION_RESUME
        }
        val pauseResumePending = PendingIntent.getService(
            context, 1, pauseResumeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextIntent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_NEXT
        }
        val nextPending = PendingIntent.getService(
            context, 2, nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val closeIntent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_CLOSE
        }
        val closePending = PendingIntent.getService(
            context, 3, closeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context, 100, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, channelId)
            .setContentTitle(songTitle)
            .setSmallIcon(R.drawable.logoapp)
            .setOngoing(isPlaying)
            .setContentIntent(openAppPendingIntent)
            .addAction(R.drawable.baseline_skip_previous_24, "", prevPending)
            .addAction(
                if (isPlaying) R.drawable.baseline_pause_24 else R.drawable.baseline_play_arrow_24,
                "",
                pauseResumePending
            )
            .addAction(R.drawable.baseline_skip_next_24, "", nextPending)
            .addAction(R.drawable.outline_close_24, "", closePending)
            .setOnlyAlertOnce(true)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2, 3)
            )
            .build()
    }
}
