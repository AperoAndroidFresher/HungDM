package com.example.hungdm.data.db.repo

import com.example.hungdm.data.db.dao.UserDao
import com.example.hungdm.data.db.entity.UserEntity
import com.example.hungdm.domain.repo.UserRepository

class UserRepositoryImpl(private val userDao: UserDao): UserRepository {

    override suspend fun getUserById(userId: Long): UserEntity? {
        return userDao.getUserById(userId)
    }

    override suspend fun signup(username: String, password: String, email: String): Long {
        val user = userDao.getUserByUsername(username)
        if (user != null) {
            return -1
        }
        val newUser = UserEntity(
            username = username,
            password = password,
            email = email
        )
        return userDao.signup(newUser)
    }

    override suspend fun login(username: String, password: String): UserEntity? {
        return userDao.login(username, password)
    }

    override suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }
}
