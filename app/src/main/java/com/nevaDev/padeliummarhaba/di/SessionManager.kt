package com.nevaDev.padeliummarhaba.di

import android.content.Context
import android.content.SharedPreferences


class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .putLong(KEY_LAST_ACTIVE, System.currentTimeMillis()) // ✅ Store last active time
            .apply()
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        val token = getAuthToken()
        val lastActive = prefs.getLong(KEY_LAST_ACTIVE, 0)
        val currentTime = System.currentTimeMillis()
        val inactiveDuration = currentTime - lastActive

        return !token.isNullOrEmpty() && inactiveDuration < SESSION_TIMEOUT
    }

    fun updateLastActiveTime() {
        prefs.edit().putLong(KEY_LAST_ACTIVE, System.currentTimeMillis()).apply()
    }

    fun logout() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
    }

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_LAST_ACTIVE = "last_active"
        private const val SESSION_TIMEOUT = 2 * 60 * 1000 // 2 minutes in milliseconds
    }

    fun isTokenExpired(): Boolean {
        val lastActive = prefs.getLong(KEY_LAST_ACTIVE, 0)
        val currentTime = System.currentTimeMillis()
        val inactiveDuration = currentTime - lastActive

        return inactiveDuration >= SESSION_TIMEOUT
    }

    fun invalidateToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply() // Remove the token if expired
    }
}
