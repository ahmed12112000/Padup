package com.padelium.data.datasource.remote

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager1  private constructor(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _isLoggedInFlow = MutableStateFlow(false)
    val isLoggedInFlow: StateFlow<Boolean> = _isLoggedInFlow.asStateFlow()

    init {
        checkAndHandleSessionValidity()
    }

    // ✅ Save authentication token (if needed)
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

    // ✅ Save JSESSIONID from login response
    fun saveJSessionId(jsessionId: String) {
        prefs.edit()
            .putString(KEY_JSESSION_ID, jsessionId)
            .apply()
    }

    // ✅ Retrieve JSESSIONID for future API calls
    fun getJSessionId(): String? {
        return prefs.getString(KEY_JSESSION_ID, null)
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

    fun clearJSessionId() {
        prefs.edit().remove(KEY_JSESSION_ID).apply()
    }

    private fun updateLoginState(isLoggedIn: Boolean) {
        _isLoggedInFlow.value = isLoggedIn
    }

    // ✅ Logout: Clear both token and JSESSIONID
    fun logout() {
        clearAuthToken()
        clearJSessionId()
    }

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_JSESSION_ID = "JSESSIONID" // ✅ Key for storing JSESSIONID

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SessionManager1? = null

        fun getInstance(context: Context): SessionManager1 {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager1(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    // ✅ Invalidate session by clearing JSESSIONID
    fun invalidateSession() {
        prefs.edit().remove(KEY_JSESSION_ID).apply()
    }
}