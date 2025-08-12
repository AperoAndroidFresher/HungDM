package com.example.hungdm.data.mapper

import com.example.hungdm.data.db.entity.SongEntity
import com.example.hungdm.data.db.entity.UserEntity
import com.example.hungdm.data.remote.songApi.SongDTO
import com.example.hungdm.domain.model.Song
import com.example.hungdm.domain.model.UserInfo

fun Song.toSongEntity(): SongEntity {
    return SongEntity(
        title = title,
        artist = artist,
        duration = duration,
        uri = uri!!,
        img = img
    )
}

fun SongEntity.toSong(): Song {
    return Song(
        id = songId,
        title = title,
        artist = artist,
        duration = duration,
        uri = uri,
        img = img
    )
}

fun SongDTO.toSong(): Song {
    return Song(
        title = title,
        artist = artist,
        duration = duration.toLong(),
        kind = kind,
        path = path
    )
}

fun UserInfo.toUserEntity(): UserEntity {
    return UserEntity(
        userId = id,
        username = username,
        password = password,
        name = name,
        phone = phone,
        email = email,
        uni = uni,
        desc = desc,
        img = img
    )
}

fun UserEntity.toUserInfo(): UserInfo {
    return UserInfo(
        id = userId,
        username = username,
        password = password,
        email = email,
        name = name,
        phone = phone,
        uni = uni,
        desc = desc,
        img = img
    )
}