package com.example.hungdm.utils

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import com.example.hungdm.domain.model.Song
import java.util.Locale

object AppUtils {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "logged_in_user"

    val color = listOf(
        Color(0xFFFF7777),
        Color(0xFFFFFA77),
        Color(0xFF4462FF),
        Color(0xFF14FF00),
        Color(0xFFE231FF),
        Color(0xFF00FFFF),
        Color(0xFFFB003C),
        Color(0xFFF2A5FF)
    )

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

    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
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

    fun isValid(s: String): Boolean {
        return s.matches("^[a-zA-Z0-9]+$".toRegex()) && noSpace(s)
    }

    fun isValidPass(password: String): Boolean {
        return password.matches("^[a-zA-Z0-9]+$".toRegex()) && noSpace(password)
    }

    fun isValidPass2(password: String, password2: String): Boolean {
        return password == password2 && isValidPass(password)
    }

    fun isValidPhone(str: String): Boolean {
        val regex = Regex("^\\d+$")
        return regex.matches(str) && str.isNotEmpty()
    }

    fun isValidEmail(email: String): Boolean {
        val regex = "^[a-zA-Z0-9._-]+@apero\\.vn$".toRegex()
        return email.matches(regex) && noSpace(email)
    }

    fun shareSong(context: Context, song: Song) {
        val uri = song.uri

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/*"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Share song")
            putExtra(Intent.EXTRA_TEXT, "Check out this song: ${song.title} by ${song.artist}")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Send"))
    }

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        return context
    }

    fun getSavedLangCode(context: Context): String {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("lang_code", "en") ?: "en"
    }

    fun setAppLanguage(langCode: String, context: Context) {
        saveLangCode(context, langCode)
        val locale = Locale(langCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)

        if (context is Activity) {
            context.window.decorView.requestLayout()
        }
    }

    private fun saveLangCode(context: Context, langCode: String) {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE).edit {
            putString("lang_code", langCode)
        }
    }

    private fun noSpace(input: String): Boolean {
        return !input.contains("\\s".toRegex())
    }
}