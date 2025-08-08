package com.example.hungdm.utils

import android.content.Context
import androidx.core.content.edit

object UserPreferences {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "logged_in_user"

    fun saveUser(context: Context, userId: Long) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit { putLong(KEY_USER_ID, userId) }
    }

    fun getUser(context: Context): Long? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val userId = sharedPref.getLong(KEY_USER_ID, -1L)
        return if (userId != -1L) userId else null
    }

    fun clear(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit { clear() }
    }
}
