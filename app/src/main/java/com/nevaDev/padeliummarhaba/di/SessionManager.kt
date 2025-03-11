package com.nevaDev.padeliummarhaba.di

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class SessionManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isLoggedInFlow = MutableStateFlow(false)
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    init {
        checkAndHandleSessionValidity()
    }

    fun saveAuthToken(token: String) {
        prefs.edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
        updateLoginState(true)
    }

    fun getAuthToken(): String? {
        return prefs.getString(KEY_AUTH_TOKEN, null)
    }

    fun isLoggedIn(): Boolean {
        return !prefs.getString(KEY_AUTH_TOKEN, null).isNullOrEmpty()
    }

    private fun checkAndHandleSessionValidity() {
        if (isLoggedIn()) {
            updateLoginState(true)
        } else {
            updateLoginState(false)
        }
    }

    fun clearAuthToken() {
        prefs.edit().remove(KEY_AUTH_TOKEN).apply()
        updateLoginState(false)
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        _isLoggedInFlow.value = isLoggedIn
    }

    fun logout() {
        clearAuthToken()
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
