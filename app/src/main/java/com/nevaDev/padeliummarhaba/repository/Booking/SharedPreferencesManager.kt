package com.nevaDev.padeliummarhaba.repository.Booking

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }
}
fun getTokenFromSharedPreferences(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("your_shared_pref_name", Context.MODE_PRIVATE)
    return sharedPreferences.getString("token_key", null)
}