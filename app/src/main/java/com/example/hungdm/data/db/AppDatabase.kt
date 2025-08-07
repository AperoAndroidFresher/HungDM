package com.example.hungdm.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hungdm.data.db.dao.PlaylistDao
import com.example.hungdm.data.db.dao.UserDao
import com.example.hungdm.data.db.entity.PlaylistEntity
import com.example.hungdm.data.db.entity.PlaylistSongReference
import com.example.hungdm.data.db.entity.SongEntity
import com.example.hungdm.data.db.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        PlaylistEntity::class,
        SongEntity::class,
        PlaylistSongReference::class
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun playlistDao(): PlaylistDao
}