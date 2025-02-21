package com.nevaDev.padeliummarhaba.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val _isLoggedInFlow = MutableStateFlow(isTokenValid())
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow

    fun saveAuthToken(token: String) {
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
        updateLastActiveTime() // Ensure session is refreshed after login
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return isTokenValid() // ✅ Fetch fresh token
    }

    private fun isTokenValid(): Boolean {
        val token = getAuthToken()
        return !token.isNullOrEmpty()
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
        _isLoggedInFlow.value = false // ✅ Update flow when user logs out
    }

    fun updateLastActiveTime() {
        val currentTime = System.currentTimeMillis()
        prefs.edit()
            .putLong(KEY_LAST_ACTIVE_TIME, currentTime)
            .apply()
    }

    fun getLastActiveTime(): Long {
        return prefs.getLong(KEY_LAST_ACTIVE_TIME, 0L)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_LAST_ACTIVE_TIME = "last_active_time" // ✅ Fixed here

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
