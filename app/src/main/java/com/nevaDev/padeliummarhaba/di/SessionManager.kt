package com.nevaDev.padeliummarhaba.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        val token = getAuthToken()
        return !token.isNullOrEmpty()
    }

    fun updateLastActiveTime() {
        // No need to store last active time anymore
    }

    fun logout() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_AUTH_TOKEN = "auth_token"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SessionManager? = null

        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun invalidateToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply() // Remove the token when invalidated
    }
}
