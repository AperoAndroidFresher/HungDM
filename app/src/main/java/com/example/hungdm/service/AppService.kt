package com.example.hungdm.service

import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt

class AppService : LifecycleService() {

    companion object {
        const val ACTION_PLAY = "ACTION_PLAY"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_RESUME = "ACTION_RESUME"
        const val ACTION_CLOSE = "ACTION_CLOSE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
        const val ACTION_UPDATE = "ACTION_UPDATE"
        const val ACTION_SHUFFLE = "ACTION_SHUFFLE"
        const val ACTION_REPEAT = "ACTION_REPEAT"
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
        val isShuffle: MutableStateFlow<Boolean> = MutableStateFlow(false)
        val isRepeat: MutableStateFlow<Boolean> = MutableStateFlow(false)
    }

    private var mediaPlayer: MediaPlayer? = null
    private var notificationHelper: NotificationHelper? = null
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
                if(isShuffle.value){
                    shuffleSong()
                } else if(isRepeat.value){
                    repeatSong()
                } else nextSong()

            }
            ACTION_PREVIOUS -> {
                if(isShuffle.value){
                    shuffleSong()
                } else if(isRepeat.value){
                    repeatSong()
                } else previousSong()
            }
            ACTION_SHUFFLE -> {
                isShuffle.value = !isShuffle.value
                isRepeat.value = false
            }
            ACTION_REPEAT -> {
                isRepeat.value = !isRepeat.value
                isShuffle.value = false
            }
            ACTION_UPDATE -> {
                playerPlaylist.value = intent.getParcelableExtra<Playlist>(EXTRA_PLAYLIST)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopForeground(true)
        super.onDestroy()
    }

    private fun playSong() {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(applicationContext, playerSong.value?.uri!!)
                prepare()
                start()
                isLooping = false
                setOnCompletionListener {
                    if(isShuffle.value){
                        shuffleSong()
                    } else if(isRepeat.value){
                        repeatSong()
                    } else nextSong()
                }
            }
            isPlay.value = true
            startUpdatingTime()
            startForeground(
                1,
                notificationHelper?.createNotification(playerSong.value?.title!!, isPlay.value)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun pauseSong() {
        timeJob?.cancel()
        mediaPlayer?.pause()
        startForeground(
            1,
            notificationHelper?.createNotification(playerSong.value?.title!!, isPlay.value)
        )
    }

    private fun closeSong() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        notificationHelper = null
        stopSelf()
    }

    private fun resumeSong() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                startUpdatingTime()
                startForeground(
                    1,
                    notificationHelper?.createNotification(playerSong.value?.title!!, isPlay.value)
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

    private fun shuffleSong() {
        if(playerPlaylist.value==null){
            val nextIndex = Random.nextInt(0, playerListSong.value!!.size)

            playerSongIndex.value = nextIndex
            playerSong.value = playerListSong.value!![nextIndex]
            playerTime.value = 0L
        } else {
            val nextIndex = Random.nextInt(0, playerPlaylist.value!!.listSong.size)

            playerSongIndex.value = nextIndex
            playerSong.value = playerPlaylist.value!!.listSong[nextIndex]
            playerTime.value = 0L
        }

        playSong()
    }

    private fun repeatSong() {
        if(playerPlaylist.value==null){
            val nextIndex = playerSongIndex.value!!

            playerSongIndex.value = nextIndex
            playerSong.value = playerListSong.value!![nextIndex]
            playerTime.value = 0L
        } else {
            val nextIndex = playerSongIndex.value!!

            playerSongIndex.value = nextIndex
            playerSong.value = playerPlaylist.value!!.listSong[nextIndex]
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
