package com.nevaDev.padeliummarhaba.di

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

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
            .find { it.startsWith("XSRF-TOKEN=") }

        xsrfTokenHeader?.let { header ->
            val csrfToken = header.substringAfter("XSRF-TOKEN=").substringBefore(";")
            println("Extracted CSRF Token: $csrfToken")
            sharedPreferences.edit().putString("XSRF-TOKEN", csrfToken).apply()
        } ?: println("CSRF Token not found in Set-Cookie header.")

        val savedCsrfToken = sharedPreferences.getString("XSRF-TOKEN", null)
        val newRequest = originalRequest.newBuilder().apply {
            savedCsrfToken?.let { token -> addHeader("X-XSRF-TOKEN", token) }
        }.build()

        originalResponse.close()
        return chain.proceed(newRequest)
    }
}
class AuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Retrieve the authentication token from SharedPreferences
        val authToken = sharedPreferences.getString("AUTH_TOKEN", null)
        println("Authorization Token: $authToken") // Log token retrieval

        val newRequestBuilder = originalRequest.newBuilder()

        // Add the Authorization header if token exists
        authToken?.let { token ->
            newRequestBuilder.addHeader("Authorization", "Bearer $token")
            println("Authorization Token Added: $token") // Log when header is added
        } ?: println("No Authorization Token found.")

        // Log the request headers to check if Authorization header is added
        val finalRequest = newRequestBuilder.build()
        println("Request Headers: ${finalRequest.headers}") // Log all headers
        sharedPreferences.edit().putString("AUTH_TOKEN", authToken).apply()

        // Proceed with the request
        return chain.proceed(finalRequest)
    }
}





