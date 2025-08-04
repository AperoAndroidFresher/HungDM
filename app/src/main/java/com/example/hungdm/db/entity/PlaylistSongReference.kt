package com.example.hungdm.db.entity

import androidx.room.Entity

@Entity(tableName = "playlistSongReference", primaryKeys = ["playlistId", "songId"])
data class PlaylistSongReference(
    val playlistId: Int,
    val songId: Long
)