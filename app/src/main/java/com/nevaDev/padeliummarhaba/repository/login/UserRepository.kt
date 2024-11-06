package com.nevaDev.padeliummarhaba.repository.login


import android.content.Context
import android.content.SharedPreferences

class UserRepository(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    var isUserLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("isLoggedIn", false)
        set(value) {
            sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()
        }
}