package com.example.hungdm.service

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.hungdm.NotificationHelper
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
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
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val EXTRA_PLAYLIST = "EXTRA_PLAYLIST"
        const val EXTRA_LIST_SONG = "EXTRA_LIST_SONG"
        const val EXTRA_INDEX = "EXTRA_INDEX"
        const val EXTRA_SONG = "EXTRA_SONG"

        val playerPlaylist: MutableStateFlow<Playlist?> = MutableStateFlow(null)
        val playerListSong: MutableStateFlow<List<Song>?> = MutableStateFlow(null)
        val playerSongIndex: MutableStateFlow<Int?> = MutableStateFlow(null)
        val playerSong: MutableStateFlow<Song?> = MutableStateFlow(null)
        val playerTime: MutableStateFlow<Long> = MutableStateFlow(0L)
        val isPlay: MutableStateFlow<Boolean> = MutableStateFlow(false)
    }

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var notificationHelper: NotificationHelper
    private var timeJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        notificationHelper = NotificationHelper(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.action) {
            ACTION_PLAY -> {
                playerPlaylist.value = intent.getParcelableExtra<Playlist>(EXTRA_PLAYLIST)
                playerListSong.value = intent.getParcelableArrayListExtra<Song>(EXTRA_LIST_SONG)
                playerSongIndex.value = intent.getIntExtra(EXTRA_INDEX,0)
                playerSong.value = intent.getParcelableExtra<Song>(EXTRA_SONG)
                playerTime.value = 0L
                isPlay.value = true
                playSong()
            }
            ACTION_PAUSE -> {
                isPlay.value = false
                pauseSong()
            }
            ACTION_CLOSE -> {
                playerPlaylist.value = null
                playerListSong.value = null
                playerSongIndex.value = null
                playerSong.value = null
                playerTime.value = 0L
                isPlay.value = false
                closeSong()
            }
            ACTION_RESUME -> {
                isPlay.value = true
                resumeSong()
            }
            ACTION_NEXT -> {
                nextSong()
            }
            ACTION_PREVIOUS -> {
                previousSong()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    private fun playSong() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, playerSong.value!!.uri!!)
                prepare()
                start()
                isLooping = false
                setOnCompletionListener {
                    nextSong()
                }
            }
            isPlay.value = true
            startUpdatingTime()
            startForeground(
                1,
                notificationHelper.createNotification("Playing", playerSong.value!!.uri!!, true)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseSong() {
        mediaPlayer?.pause()
        startForeground(
            1,
            notificationHelper.createNotification("Paused", playerSong.value!!.uri, false)
        )
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
                    notificationHelper.createNotification("Playing", playerSong.value!!.uri, true)
                )
            }
        }
    }

    private fun nextSong() {
        if(playerPlaylist.value==null){
            val nextIndex = if (playerSongIndex.value!! >= playerListSong.value!!.size - 1) 0 else playerSongIndex.value!! + 1

            playerSongIndex.value = nextIndex
            playerSong.value = playerListSong.value!![nextIndex]
            playerTime.value = 0L
        } else {
            val nextIndex = if (playerSongIndex.value!! >= playerPlaylist.value!!.listSong.size - 1) 0 else playerSongIndex.value!! + 1

            playerSongIndex.value = nextIndex
            playerSong.value = playerPlaylist.value!!.listSong[nextIndex]
            playerTime.value = 0L
        }

        playSong()
    }

    private fun previousSong(){
        if(playerPlaylist.value==null){
            val preIndex = if (playerSongIndex.value!! <= 0) playerListSong.value!!.size - 1 else playerSongIndex.value!! - 1

            playerSongIndex.value = preIndex
            playerSong.value = playerListSong.value!![preIndex]
            playerTime.value = 0L
        } else {
            val preIndex = if (playerSongIndex.value!! <= 0) playerPlaylist.value!!.listSong.size - 1 else playerSongIndex.value!! - 1

            playerSongIndex.value = preIndex
            playerSong.value = playerPlaylist.value!!.listSong[preIndex]
            playerTime.value = 0L
        }

        playSong()
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
