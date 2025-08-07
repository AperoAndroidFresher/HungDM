package com.example.hungdm.model

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri

data class Song(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val duration: Long,
    val albumArt: String? = "",
    val uri: Uri = "".toUri(),
    val albumArtUri: Uri? = null
){
    val time = formatDuration(duration)
}

fun formatDuration(durationMs: Long): String {
    val minutes = durationMs / 1000 / 60
    val seconds = (durationMs / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}

fun getAlbumArt(context: Context, albumId: Long): String? {
    val uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
    val selection = "${MediaStore.Audio.Albums._ID}=?"
    val selectionArgs = arrayOf(albumId.toString())

    context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
        }
    }
    return null
}