package com.example.hungdm.utils

import android.content.Context
import com.example.hungdm.data.db.entity.UserEntity
import com.google.gson.Gson

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER = "logged_in_user"

    private val gson = Gson()

    fun saveUser(context: Context, user: UserEntity) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userJson = gson.toJson(user)
        sharedPref.edit().putString(KEY_USER, userJson).apply()
    }

    fun getUser(context: Context): UserEntity? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userJson = sharedPref.getString(KEY_USER, null) ?: return null
        return try {
            gson.fromJson(userJson, UserEntity::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clear(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()
    }
}
