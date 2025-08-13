package com.example.hungdm.service

import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
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
                close()
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
                if(!playerPlaylist.value!!.listSong.map { it.id }.contains(playerSong.value?.id)){
                    if(playerPlaylist.value!!.listSong.isNotEmpty()) nextSong() else close()
                    Log.d("Service", "remove")
                }
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

    private fun close() {
        playerPlaylist.value = null
        playerListSong.value = null
        playerSongIndex.value = null
        playerSong.value = null
        playerTime.value = 0L
        isPlay.value = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        notificationHelper = null
        timeJob?.cancel()
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

    private fun ensureNotification() {
        val title = playerSong.value?.title ?: "Playing"
        val playing = isPlay.value
        val notif = (notificationHelper ?: NotificationHelper(this))
            .also { notificationHelper = it }
            .createNotification(title, playing)
        // Nếu đã ở foreground thì update, còn chưa thì startForeground
        if (playing) {
            startForeground(1, notif)
        } else {
            // update notification khi pause
            startForeground(1, notif)
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




//class AppService : LifecycleService() {
//
//    companion object {
//        const val ACTION_PLAY = "ACTION_PLAY"
//        const val ACTION_PAUSE = "ACTION_PAUSE"
//        const val ACTION_RESUME = "ACTION_RESUME"
//        const val ACTION_CLOSE = "ACTION_CLOSE"
//        const val ACTION_NEXT = "ACTION_NEXT"
//        const val ACTION_PREVIOUS = "ACTION_PREVIOUS"
//        const val ACTION_UPDATE = "ACTION_UPDATE"
//        const val ACTION_SHUFFLE = "ACTION_SHUFFLE"
//        const val ACTION_REPEAT = "ACTION_REPEAT"
//
//        const val EXTRA_PLAYLIST = "EXTRA_PLAYLIST"
//        const val EXTRA_LIST_SONG = "EXTRA_LIST_SONG"
//        const val EXTRA_INDEX = "EXTRA_INDEX"
//        const val EXTRA_SONG = "EXTRA_SONG"
//
//        val playerPlaylist = MutableStateFlow<Playlist?>(null)
//        val playerListSong = MutableStateFlow<List<Song>?>(null)
//        val playerSongIndex = MutableStateFlow<Int?>(null)
//        val playerSong = MutableStateFlow<Song?>(null)
//        val playerTime = MutableStateFlow(0L)
//        val isPlay = MutableStateFlow(false)
//        val isShuffle = MutableStateFlow(false)
//        val isRepeat = MutableStateFlow(false)
//    }
//
//    private var mediaPlayer: MediaPlayer? = null
//    private var notificationHelper: NotificationHelper? = null
//    private var timeJob: Job? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        notificationHelper = NotificationHelper(this)
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        super.onStartCommand(intent, flags, startId)
//
//        val action = intent?.action ?: return START_STICKY
//        when (action) {
//            ACTION_PLAY -> {
//                val pl = intent.getParcelableExtra<Playlist>(EXTRA_PLAYLIST)
//                val list = intent.getParcelableArrayListExtra<Song>(EXTRA_LIST_SONG)
//                val index = intent.getIntExtra(EXTRA_INDEX, -1)
//                val song = intent.getParcelableExtra<Song>(EXTRA_SONG)
//
//                if ((pl == null && list.isNullOrEmpty()) || index < 0 || song == null) {
//                    // Thiếu dữ liệu, bỏ qua để tránh crash
//                    return START_STICKY
//                }
//
//                playerPlaylist.value = pl
//                playerListSong.value = list
//                playerSongIndex.value = index
//                playerSong.value = song
//                playerTime.value = 0L
//                isPlay.value = true
//                playSong()
//            }
//
//            ACTION_PAUSE -> {
//                isPlay.value = false
//                pauseSong()
//            }
//
//            ACTION_CLOSE -> {
//                resetState()
//                closeSong()
//            }
//
//            ACTION_RESUME -> {
//                isPlay.value = true
//                resumeSong()
//            }
//
//            ACTION_NEXT -> when {
//                isShuffle.value -> shuffleSong()
//                isRepeat.value -> repeatSong()
//                else -> nextSong()
//            }
//
//            ACTION_PREVIOUS -> when {
//                isShuffle.value -> shuffleSong()
//                isRepeat.value -> repeatSong()
//                else -> previousSong()
//            }
//
//            ACTION_SHUFFLE -> {
//                isShuffle.value = !isShuffle.value
//                if (isShuffle.value) isRepeat.value = false
//                // Không đổi bài ngay để giữ UX; nếu muốn đổi ngay có thể gọi shuffleSong()
//            }
//
//            ACTION_REPEAT -> {
//                isRepeat.value = !isRepeat.value
//                if (isRepeat.value) isShuffle.value = false
//            }
//
//            ACTION_UPDATE -> {
//                playerPlaylist.value = intent.getParcelableExtra(EXTRA_PLAYLIST)
//                // Không đổi bài nếu index/song không đổi
//            }
//        }
//        return START_STICKY
//    }
//
//    override fun onDestroy() {
//        timeJob?.cancel()
//        timeJob = null
//        mediaPlayer?.release()
//        mediaPlayer = null
//        stopForeground(true)
//        super.onDestroy()
//    }
//
//    // ---------- Playback ----------
//
//    private fun playSong() {
//        val uri = playerSong.value?.uri ?: return
//        try {
//            timeJob?.cancel()
//            mediaPlayer?.release()
//            mediaPlayer = MediaPlayer().apply {
//                setDataSource(applicationContext, uri)
//                setOnPreparedListener {
//                    start()
//                    isLooping = false
//                    startUpdatingTime()
//                    ensureNotification()
//                }
//                setOnCompletionListener {
//                    when {
//                        isShuffle.value -> shuffleSong()
//                        isRepeat.value -> repeatSong()
//                        else -> nextSong()
//                    }
//                }
//                prepareAsync()
//            }
//            isPlay.value = true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // Thất bại -> thử dừng sạch để tránh treo
//            pauseSong()
//        }
//    }
//
//    private fun pauseSong() {
//        timeJob?.cancel()
//        timeJob = null
//        mediaPlayer?.pause()
//        ensureNotification()
//    }
//
//    private fun closeSong() {
//        timeJob?.cancel()
//        timeJob = null
//        mediaPlayer?.stop()
//        mediaPlayer?.release()
//        mediaPlayer = null
//        stopForeground(true)
//        stopSelf()
//    }
//
//    private fun resumeSong() {
//        mediaPlayer?.let {
//            if (!it.isPlaying) {
//                it.start()
//                startUpdatingTime()
//                ensureNotification()
//            }
//        }
//    }
//
//    private fun nextSong() {
//        val (list, idx) = currentListAndIndex() ?: return
//        if (list.isEmpty()) return
//
//        val nextIndex = if (idx >= list.lastIndex) 0 else idx + 1
//        setCurrentByIndex(nextIndex, list)
//        playSong()
//    }
//
//    private fun previousSong() {
//        val (list, idx) = currentListAndIndex() ?: return
//        if (list.isEmpty()) return
//
//        val prevIndex = if (idx <= 0) list.lastIndex else idx - 1
//        setCurrentByIndex(prevIndex, list)
//        playSong()
//    }
//
//    private fun shuffleSong() {
//        val (list, _) = currentListAndIndex() ?: return
//        if (list.isEmpty()) return
//
//        val nextIndex = Random.nextInt(0, list.size)
//        setCurrentByIndex(nextIndex, list)
//        playSong()
//    }
//
//    private fun repeatSong() {
//        val (list, idx) = currentListAndIndex() ?: return
//        if (list.isEmpty() || idx !in list.indices) return
//
//        setCurrentByIndex(idx, list)
//        playSong()
//    }
//
//    // ---------- Helpers ----------
//
//    private fun currentListAndIndex(): Pair<List<Song>, Int>? {
//        val list = playerPlaylist.value?.listSong ?: playerListSong.value ?: emptyList()
//        val idx = playerSongIndex.value ?: -1
//        return if (idx in list.indices) list to idx else null
//    }
//
//    private fun setCurrentByIndex(index: Int, list: List<Song>) {
//        playerSongIndex.value = index
//        playerSong.value = list[index]
//        playerTime.value = 0L
//    }
//
//    private fun startUpdatingTime() {
//        timeJob?.cancel()
//        timeJob = lifecycleScope.launch {
//            while (isActive) {
//                val t = mediaPlayer?.currentPosition?.toLong() ?: 0L
//                playerTime.value = t
//                delay(100)
//            }
//        }
//    }
//
//    private fun ensureNotification() {
//        val title = playerSong.value?.title ?: "Playing"
//        val playing = isPlay.value
//        val notif = (notificationHelper ?: NotificationHelper(this))
//            .also { notificationHelper = it }
//            .createNotification(title, playing)
//        // Nếu đã ở foreground thì update, còn chưa thì startForeground
//        if (playing) {
//            startForeground(1, notif)
//        } else {
//            // update notification khi pause
//            startForeground(1, notif)
//        }
//    }
//
//    private fun resetState() {
//        playerPlaylist.value = null
//        playerListSong.value = null
//        playerSongIndex.value = null
//        playerSong.value = null
//        playerTime.value = 0L
//        isPlay.value = false
//    }
//}
