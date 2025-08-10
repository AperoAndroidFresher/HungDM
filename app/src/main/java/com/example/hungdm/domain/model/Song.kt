package com.example.hungdm.domain.model

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Parcelable
import com.example.hungdm.utils.AppUtils
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: Long = 0,
    val title: String,
    val artist: String,
    val duration: Long,
    val uri: Uri? = null,
    val img: ByteArray? = null,
    val kind: String? = null,
    val path: String? = null
): Parcelable {
    val time = AppUtils.formatTime(duration)
}
