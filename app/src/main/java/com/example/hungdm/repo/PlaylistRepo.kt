package com.example.hungdm.repo

import com.example.hungdm.db.dao.PlaylistDao
import com.example.hungdm.db.entity.PlaylistEntity

interface PlaylistRepository{
    suspend fun createPlaylist(playlist: PlaylistEntity): Long
    suspend fun renamePlaylist(playlistId: Int, newTitle: String)
    suspend fun deletePlaylist(playlist: PlaylistEntity)
    suspend fun getPlaylistsOfUser(userId: Int): List<PlaylistEntity>
}

class PlaylistRepositoryImpl(private val playlistDao: PlaylistDao): PlaylistRepository{
    override suspend fun createPlaylist(playlist: PlaylistEntity): Long {
        return playlistDao.createPlaylist(playlist)
    }

    override suspend fun renamePlaylist(playlistId: Int, newTitle: String) {
        playlistDao.renamePlaylist(playlistId,newTitle)
    }

    override suspend fun deletePlaylist(playlist: PlaylistEntity) {
        playlistDao.deletePlaylist(playlist)
    }

    override suspend fun getPlaylistsOfUser(userId: Int): List<PlaylistEntity> {
        return playlistDao.getPlaylistsOfUser(userId)
    }
}