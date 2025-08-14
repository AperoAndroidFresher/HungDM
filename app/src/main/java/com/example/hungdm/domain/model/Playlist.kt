package com.example.hungdm.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.util.UUID

@Parcelize
data class Playlist(
    val id: Long = 0,
    val title:String ="",
    val listSong: MutableList<Song> = mutableListOf(),
): Parcelable {
    val songNumber = listSong.size
}