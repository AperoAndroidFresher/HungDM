package com.example.hungdm.domain.repo

import com.example.hungdm.data.db.entity.UserEntity

interface UserRepository{
    suspend fun getUserById(userId: Long): UserEntity?
    suspend fun signup(username: String, password: String, email: String): Long
    suspend fun login(username: String, password: String): UserEntity?
    suspend fun updateUser(user: UserEntity)
}