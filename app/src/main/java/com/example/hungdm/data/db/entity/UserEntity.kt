package com.example.hungdm.data.db.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.hungdm.data.db.converters.Converters

@Entity(tableName = "users")
@TypeConverters(Converters::class)
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val userId: Long=0,
    val username: String = "",
    val password: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val uni: String = "",
    val desc: String = "",
    val img: ByteArray? = null
)
