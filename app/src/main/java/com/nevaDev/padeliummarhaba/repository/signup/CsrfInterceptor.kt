package com.nevaDev.padeliummarhaba.repository.signup
// CsrfInterceptor.kt

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CsrfInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader("X-XSRF-TOKEN", getCsrfToken()) // Replace with your method to get the CSRF token
            .build()
        return chain.proceed(newRequest)
    }

    private fun getCsrfToken(): String {
        // Logic to retrieve CSRF token from your storage or cookie
        return "" // Replace with actual token retrieval logic
    }
}
