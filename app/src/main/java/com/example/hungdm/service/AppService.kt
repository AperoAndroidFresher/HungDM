package com.example.hungdm.service

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.hungdm.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class AppService : LifecycleService() {

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_CLOSE = "ACTION_CLOSE"
        const val EXTRA_URI = "EXTRA_URI"

        val playerTime = MutableStateFlow(0L)
        val playerDuration = MutableStateFlow(0L)
        val isPlaying = MutableStateFlow(false)
    }

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var notificationHelper: NotificationHelper
    private var songUri: Uri? = null
    private var timeJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PLAY -> {
                val uri = intent.getParcelableExtra<Uri>(EXTRA_URI)
                uri?.let {
                    playSong(it)
                }
            }
            ACTION_PAUSE -> {
                pauseSong()
            }
            ACTION_CLOSE -> {
                closeSong()
            }
            ACTION_RESUME -> {
                resumeSong()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    private fun playSong(uri: Uri) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, uri)
                prepare()
                start()
                isLooping = false
            }
            songUri = uri
            playerDuration.value = mediaPlayer?.duration?.toLong() ?: 0L
            isPlaying.value = true
            startUpdatingTime()
            startForeground(
                1,
                notificationHelper.createNotification("Playing", uri, true)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
        startForeground(
            1,
            notificationHelper.createNotification("Paused", songUri, false)
        )
        Log.d("tag", "pauseSong: ${playerTime.value}")
    }

    private fun closeSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
    }

    private fun resumeSong() {
        timeJob?.cancel()
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                startUpdatingTime()
                startForeground(
                    1,
                    notificationHelper.createNotification("Playing", songUri, true)
                )
                Log.d("tag", "resumeSong: ${playerTime.value}")
            }
        }
    }

    private fun startUpdatingTime() {
        timeJob?.cancel()
        timeJob = lifecycleScope.launch {
            while (isActive) {
                val time = mediaPlayer?.currentPosition?.toLong() ?: 0L
                playerTime.value = time
                delay(100)
            }
        }
    }
}
