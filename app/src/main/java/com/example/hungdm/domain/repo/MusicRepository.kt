package com.example.hungdm.domain.repo

import android.content.Context
import android.content.Intent
import com.example.hungdm.domain.model.Playlist
import com.example.hungdm.domain.model.Song
import com.example.hungdm.service.AppService

interface MusicRepository {
    fun playSong(song: Song, listSong: List<Song>?, playlist: Playlist?, index: Int)
    fun pause()
    fun resume()
    fun close() // xoa playlist dang phat, huy service, logout
    fun next()
    fun previous()
    fun shuffle()
    fun repeat()
    fun seek(position: Int)
    fun updatePlaylist(playlist: Playlist) // them bai hat vao playlist, xoa bai hat dang khong phat
    fun handleCurrentSongDeleted() // xoa bai hat dang phat
}

class MusicRepositoryImpl(private val context: Context) : MusicRepository {

    override fun playSong(song: Song, listSong: List<Song>?, playlist: Playlist?, index: Int) {
        val intent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_PLAY
            putExtra(AppService.EXTRA_PLAYLIST, playlist)
            putExtra(AppService.EXTRA_LIST_SONG, ArrayList(listSong ?: emptyList()))
            putExtra(AppService.EXTRA_SONG, song)
            putExtra(AppService.EXTRA_INDEX, index)
        }
        context.startForegroundService(intent)
    }

    override fun pause() = sendAction(AppService.ACTION_PAUSE)
    override fun resume() = sendAction(AppService.ACTION_RESUME)
    override fun close() = sendAction(AppService.ACTION_CLOSE)
    override fun next() = sendAction(AppService.ACTION_NEXT)
    override fun previous() = sendAction(AppService.ACTION_PREVIOUS)
    override fun shuffle() = sendAction(AppService.ACTION_SHUFFLE)
    override fun repeat() = sendAction(AppService.ACTION_REPEAT)
    override fun handleCurrentSongDeleted() = sendAction(AppService.ACTION_HANDLE_CURRENT_SONG_DELETE)

    override fun seek(position: Int) {
        val intent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_SEEK
            putExtra(AppService.EXTRA_SEEK, position)
        }
        context.startService(intent)
    }

    override fun updatePlaylist(playlist: Playlist) {
        val intent = Intent(context, AppService::class.java).apply {
            action = AppService.ACTION_UPDATE
            putExtra(AppService.EXTRA_PLAYLIST, playlist)
        }
        context.startService(intent)
    }

    private fun sendAction(action: String) {
        val intent = Intent(context, AppService::class.java).apply { this.action = action }
        context.startService(intent)
    }
}
