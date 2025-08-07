package com.example.hungdm.domain.repo

import com.example.hungdm.data.db.entity.PlaylistEntity
import com.example.hungdm.data.db.entity.PlaylistSongReference
import com.example.hungdm.data.db.entity.PlaylistWithSongs
import com.example.hungdm.data.db.entity.SongEntity

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