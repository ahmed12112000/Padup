package com.nevaDev.padeliummarhaba.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.flow.asStateFlow
class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ✅ Initialize session validity check on app start
    private val _isLoggedInFlow = MutableStateFlow(false)
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    init {
        checkAndHandleSessionValidity() // ✅ Ensure proper session handling on app launch
    }

    fun saveAuthToken(token: String) {
        val currentTime = System.currentTimeMillis()
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .putLong(KEY_LAST_ACTIVE_TIME, currentTime) // ✅ Save last active time
            .apply()
        updateLoginState(true) // ✅ Ensure login state update
    }

    fun getAuthToken(): String? {
        return if (checkSessionValidity()) {
            prefs.getString(KEY_AUTH_TOKEN, null)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return checkSessionValidity() // ✅ Ensure session expiration check
    }

    private fun checkSessionValidity(): Boolean {
        val lastActiveTime = prefs.getLong(KEY_LAST_ACTIVE_TIME, 0L)
        val currentTime = System.currentTimeMillis()

        return if ((currentTime - lastActiveTime) > SESSION_TIMEOUT) {
            clearAuthToken() // ✅ Expire session properly
            false
        } else {
            !prefs.getString(KEY_AUTH_TOKEN, null).isNullOrEmpty()
        }
    }

    private fun checkAndHandleSessionValidity() {
        if (!checkSessionValidity()) {
            clearAuthToken() // ✅ Expire session immediately if needed
        } else {
            updateLoginState(true) // ✅ Ensure state is restored properly
        }
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
        updateLoginState(false) // ✅ Update flow when user logs out
    }

    fun updateLastActiveTime() {
        val currentTime = System.currentTimeMillis()
        prefs.edit()
            .putLong(KEY_LAST_ACTIVE_TIME, currentTime)
            .apply()
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        _isLoggedInFlow.value = isLoggedIn // ✅ Ensures UI updates correctly
    }

    fun getLastActiveTime(): Long {
        return prefs.getLong(KEY_LAST_ACTIVE_TIME, 0L)
    }

    fun logout() {
        prefs.edit().clear().apply()
        updateLoginState(false) // ✅ Ensure state is updated
    }

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_LAST_ACTIVE_TIME = "last_active_time"
        private const val SESSION_TIMEOUT = 3000_000L // ✅ 30 minutes timeout (600,000 milliseconds)

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
