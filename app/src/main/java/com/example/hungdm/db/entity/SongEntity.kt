package com.example.hungdm.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hungdm.db.converters.Converters

@Entity(tableName = "songs")
@TypeConverters(Converters::class)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Long=0,
    val title: String,
    val artist: String,
    val duration: Long,
    val albumArt: String?,
    val uri: Uri,
    val albumArtUri: Uri
)
