package com.example.hungdm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.example.hungdm.NotificationHelper
import com.example.hungdm.R

class AppService : LifecycleService() {

    companion object {
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_RESUME = "action_resume"
        const val ACTION_CLOSE = "action_close"
        const val EXTRA_URI = "extra_uri"
    }

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var notificationHelper: NotificationHelper
    private var currentUri: Uri? = null

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PLAY -> {
                val uri = intent.getParcelableExtra<Uri>(EXTRA_URI)
                Log.d("tag","onStartCommand play")
                uri?.let {
                    playMusic(it)
                }
            }
            ACTION_PAUSE -> {
                pauseMusic()
            }
            ACTION_CLOSE -> {
                stopMusic()
            }
            ACTION_RESUME -> {
                resumeMusic()
            }
        }
        return START_STICKY
    }

    private fun playMusic(uri: Uri) {
        try {
            Log.d("tag","uri")
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, uri)
                prepare()
                start()
                isLooping = false
            }
            currentUri = uri
            startForeground(
                1,
                notificationHelper.createNotification("Playing", uri, true)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
        startForeground(
            1,
            notificationHelper.createNotification("Paused", currentUri, false)
        )
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        stopSelf()
    }

    private fun resumeMusic() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                startForeground(
                    1,
                    notificationHelper.createNotification("Playing", currentUri, true)
                )
            }
        }
    }


    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }
}
