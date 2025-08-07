package com.example.hungdm.model

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.core.net.toUri

data class Song(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri? = null,
    val img: ByteArray? = null,
    val kind: String? = null,
    val path: String? = null
){
    val time = formatDuration(duration)
}

fun formatDuration(durationMs: Long): String {
    val minutes = durationMs / 1000 / 60
    val seconds = (durationMs / 1000) % 60
    return "%d:%02d".format(minutes, seconds)
}

fun getAlbumArt(context: Context, albumId: Long): ByteArray? {
    val albumArtUri = ContentUris.withAppendedId(
        Uri.parse("content://media/external/audio/albumart"), albumId
    )

    return try {
        context.contentResolver.openInputStream(albumArtUri)?.use { inputStream ->
            inputStream.readBytes()
        }
    } catch (e: Exception) {
        null
    }
}
