package com.example.hungdm.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.hungdm.data.db.entity.PlaylistEntity
import com.example.hungdm.data.db.entity.PlaylistSongReference
import com.example.hungdm.data.db.entity.PlaylistWithSongs
import com.example.hungdm.data.db.entity.SongEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(song: SongEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(reference: PlaylistSongReference)

    @Query("UPDATE playlists SET title = :newTitle WHERE playlistId = :playlistId")
    suspend fun renamePlaylist(playlistId: Long, newTitle: String)

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    suspend fun removePlaylist(playlistId: Long)

    @Query("DELETE FROM playlistSongReference WHERE playlistId = :playlistId")
    suspend fun removeAllSongInPlaylist(playlistId: Long)

    @Query("DELETE FROM playlistSongReference WHERE playlistId = :playlistId AND songId = :songId")
    suspend fun removeSongInPlaylist(songId: Long, playlistId: Long)

    @Transaction
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    suspend fun getPlaylistsOfUser(userId: Long): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    suspend fun getPlaylistsWithSongsOfUser(userId: Long): List<PlaylistWithSongs>

    @Query("SELECT * FROM songs WHERE songId IN (SELECT songId FROM playlistSongReference WHERE playlistId = :playlistId)")
    suspend fun getSongsOfPlaylist(playlistId: Long): List<SongEntity>
}