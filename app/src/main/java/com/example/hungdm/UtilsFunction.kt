package com.example.hungdm

import android.content.Context
import android.content.Intent
import com.example.hungdm.model.Song

object UtilsFunction {
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

    private fun noSpace(input: String): Boolean {
        return !input.contains("\\s".toRegex())
    }
}