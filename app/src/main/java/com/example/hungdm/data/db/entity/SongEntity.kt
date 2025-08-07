package com.example.hungdm.data.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hungdm.data.db.converters.Converters

@Entity(tableName = "songs")
@TypeConverters(Converters::class)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val songId: Long=0,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri?,
    val img: ByteArray?
)
