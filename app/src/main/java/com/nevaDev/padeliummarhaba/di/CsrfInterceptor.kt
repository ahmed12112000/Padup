package com.nevaDev.padeliummarhaba.di

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class CsrfInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        println("Original Request: ${originalRequest.url}")

        val originalResponse = chain.proceed(originalRequest)

        // Log Set-Cookie headers for debugging
        originalResponse.headers("Set-Cookie").forEach {
            println("Set-Cookie Header: $it")
        }

        val xsrfTokenHeader = originalResponse.headers("Set-Cookie")
            .find { it.startsWith("") }

        xsrfTokenHeader?.let { header ->
            println("Extracted CSRF Token: ")
        } ?: println("CSRF Token not found in Set-Cookie header.")

        val savedCsrfToken = sharedPreferences.getString("XSRF-TOKEN", null)
        val newRequest = originalRequest.newBuilder()
            .build()

        originalResponse.close()
        return chain.proceed(newRequest)
    }
}









