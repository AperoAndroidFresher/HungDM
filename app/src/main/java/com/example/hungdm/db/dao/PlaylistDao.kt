package com.example.hungdm.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.hungdm.db.entity.PlaylistEntity
import com.example.hungdm.db.entity.PlaylistWithSongs
import com.example.hungdm.db.entity.SongEntity

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    @Query("UPDATE playlists SET title = :newTitle WHERE id = :playlistId")
    suspend fun renamePlaylist(playlistId: Int, newTitle: String)

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Transaction
    @Query("SELECT * FROM playlists WHERE userId = :userId")
    suspend fun getPlaylistsOfUser(userId: Int): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSongToPlaylist(songs: SongEntity)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertPlaylistSongCrossRef(crossRef: PlaylistSongCrossRef)
//
//    @Transaction
//    @Query("SELECT * FROM playlist")
//    suspend fun getPlaylistsWithSongs(): List<PlaylistWithSongs>
//
//
//    @Transaction
//    suspend fun deletePlaylistCompletely(playlistId: Long) {
//        deletePlaylistSongRelations(playlistId)
//        removePlaylist(playlistId)
//    }
//
//    @Query("DELETE FROM playlist WHERE playlistId = :playlistId")
//    suspend fun removePlaylist(playlistId: Long)
//
//    @Query("DELETE FROM playlist_song WHERE playlistId = :playlistId")
//    suspend fun deletePlaylistSongRelations(playlistId: Long)
//
//    @Query("SELECT * FROM songs WHERE songId IN (SELECT songId FROM playlist_song WHERE playlistId = :playlistId)")
//    suspend fun loadSongsFromPlaylist(playlistId: Long): List<SongEntity>
}