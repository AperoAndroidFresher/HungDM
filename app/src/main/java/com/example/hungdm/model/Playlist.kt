package com.example.hungdm.model

import java.util.UUID


data class Playlist(
    val id: Long = 0,
    val title:String ="",
    val listSong: MutableList<Song> = mutableListOf(),
) {
    val songNumber = listSong.size
    val songNumberStr =  "${listSong.size} songs"
}