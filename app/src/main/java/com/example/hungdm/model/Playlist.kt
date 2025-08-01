package com.example.hungdm.model

import java.util.UUID


data class Playlist(
    val id: String = UUID.randomUUID().toString(),
    val title:String ="",
    val listSong: MutableList<Song> = mutableListOf(),
) {
    val songNumber = listSong.size
    val songNumberStr =  "${listSong.size} songs"
}