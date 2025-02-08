package com.nevaDev.padeliummarhaba.repository.signup

// SessionManager.kt

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserSession(email: String) {
        val editor = prefs.edit()
        editor.putString("email", email)
        editor.apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString("email", null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
