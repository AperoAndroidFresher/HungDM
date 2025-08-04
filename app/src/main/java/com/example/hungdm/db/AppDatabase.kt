package com.example.hungdm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hungdm.db.dao.PlaylistDao
import com.example.hungdm.db.dao.UserDao
import com.example.hungdm.db.entity.PlaylistEntity
import com.example.hungdm.db.entity.PlaylistSongReference
import com.example.hungdm.db.entity.SongEntity
import com.example.hungdm.db.entity.UserEntity

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

//    companion object{
//        @Volatile
//        private var INSTANCES: AppDatabase?= null
//        fun getInstance(context: Context): AppDatabase{
//            return INSTANCES?: synchronized(this){
//                INSTANCES?: Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java, ""
//                ).fallbackToDestructiveMigration().build().also {
//                    INSTANCES=it
//                }
//            }
//        }
//    }
}