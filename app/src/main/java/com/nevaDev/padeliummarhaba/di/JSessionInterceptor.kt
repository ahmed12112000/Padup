package com.nevaDev.padeliummarhaba.di

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

class JSessionInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {

    companion object {
        private const val COOKIE_KEY = "JSESSIONID"
        private const val LOGIN_URL = "/api/authentication/"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Check if the request is for the /api/authentication/ endpoint
        if (request.url.encodedPath == LOGIN_URL) {
            Log.d("JSessionInterceptor", "Skipping cookie handling for ${request.url}")
            return chain.proceed(request)
        }

        val jsessionId = sharedPreferences.getString(COOKIE_KEY, null)

        Log.d("JSessionInterceptor", "Request headers: ${request.headers}")

        val modifiedRequest = if (jsessionId != null) {
            request.newBuilder()
                .addHeader("Cookie", "JSESSIONID=$jsessionId")
                .build()
        } else {
            request
        }

        val response = chain.proceed(modifiedRequest)

        response.headers("Set-Cookie").forEach { cookie ->
            Log.d("JSessionInterceptor", "Set-Cookie: $cookie")
            if (cookie.startsWith("JSESSIONID=")) {
                val jsessionId = cookie.split(";")[0].substring("JSESSIONID=".length)
                sharedPreferences.edit().putString(COOKIE_KEY, jsessionId).apply()
            }
        }

        return response
    }

}

