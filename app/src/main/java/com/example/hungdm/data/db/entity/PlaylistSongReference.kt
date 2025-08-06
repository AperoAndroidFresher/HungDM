package com.example.hungdm.data.db.entity

import androidx.room.Entity

@Entity(tableName = "playlistSongReference", primaryKeys = ["playlistId", "songId"])
data class PlaylistSongReference(
    val playlistId: Long,
    val songId: Long
)