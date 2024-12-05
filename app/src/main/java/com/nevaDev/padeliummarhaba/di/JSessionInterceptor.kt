package com.nevaDev.padeliummarhaba.di

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class JSessionInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {

    companion object {
        private const val COOKIE_KEY = "JSESSIONID"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Modify the request by adding JSESSIONID if it's available
        val jsessionId = sharedPreferences.getString(COOKIE_KEY, null)
        val modifiedRequest = if (jsessionId != null) {
            request.newBuilder()
                .addHeader("Cookie", "JSESSIONID=$jsessionId")
                .build()
        } else {
            request
        }

        // Proceed with the request
        val response = chain.proceed(modifiedRequest)

        // Extract the JSESSIONID from the Set-Cookie header if present
        response.headers("Set-Cookie").forEach { cookie ->
            if (cookie.startsWith("JSESSIONID=")) {
                val jsessionId = cookie.split(";")[0].substring("JSESSIONID=".length)
                // Save the JSESSIONID to SharedPreferences
                sharedPreferences.edit().putString(COOKIE_KEY, jsessionId).apply()
            }
        }

        return response
    }
}