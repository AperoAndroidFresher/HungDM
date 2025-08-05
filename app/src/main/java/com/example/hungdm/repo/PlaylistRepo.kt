package com.example.hungdm.repo

import com.example.hungdm.db.dao.PlaylistDao
import com.example.hungdm.db.entity.PlaylistEntity
import com.example.hungdm.db.entity.PlaylistSongReference
import com.example.hungdm.db.entity.PlaylistWithSongs
import com.example.hungdm.db.entity.SongEntity

interface PlaylistRepository{
    suspend fun createPlaylist(playlist: PlaylistEntity): Long
    suspend fun addSong(song: SongEntity): Long
    suspend fun addSongToPlaylist(reference: PlaylistSongReference)
    suspend fun renamePlaylist(playlistId: Long, newTitle: String)
    suspend fun removePlaylist(playlistId: Long)
    suspend fun removeAllSongInPlaylist(playlistId: Long)
    suspend fun removeSongInPlaylist(songId: Long, playlistId: Long)
    suspend fun getPlaylistsOfUser(userId: Long): List<PlaylistEntity>
    suspend fun getSongsOfPlaylist(playlistId: Long): List<SongEntity>
    suspend fun getPlaylistsWithSongsOfUser(userId: Long): List<PlaylistWithSongs>
}

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao): PlaylistRepository{
    override suspend fun createPlaylist(playlist: PlaylistEntity): Long {
        return playlistDao.createPlaylist(playlist)
    }

    override suspend fun addSong(song: SongEntity): Long {
        return playlistDao.addSong(song)
    }

    override suspend fun addSongToPlaylist(reference: PlaylistSongReference) {
        playlistDao.addSongToPlaylist(reference)
    }

    override suspend fun renamePlaylist(playlistId: Long, newTitle: String) {
        playlistDao.renamePlaylist(playlistId,newTitle)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        playlistDao.removePlaylist(playlistId)
    }

    override suspend fun removeAllSongInPlaylist(playlistId: Long) {
        playlistDao.removeAllSongInPlaylist(playlistId)
    }

    override suspend fun removeSongInPlaylist(songId: Long, playlistId: Long) {
        playlistDao.removeSongInPlaylist(songId, playlistId)
    }

    override suspend fun getPlaylistsWithSongsOfUser(userId: Long): List<PlaylistWithSongs> {
        return playlistDao.getPlaylistsWithSongsOfUser(userId)
    }

    override suspend fun getPlaylistsOfUser(userId: Long): List<PlaylistEntity> {
        return playlistDao.getPlaylistsOfUser(userId)
    }

    override suspend fun getSongsOfPlaylist(playlistId: Long): List<SongEntity> {
        return playlistDao.getSongsOfPlaylist(playlistId)
    }
}