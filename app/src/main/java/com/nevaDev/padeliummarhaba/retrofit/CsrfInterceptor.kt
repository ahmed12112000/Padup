package com.nevaDev.padeliummarhaba.retrofit

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class CsrfInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Proceed with the original request and capture the response
        val originalRequest = chain.request()
        val originalResponse = chain.proceed(originalRequest)

        // Log Set-Cookie headers for debugging
        originalResponse.headers("Set-Cookie").forEach {
            println("Set-Cookie Header: $it") // Log the Set-Cookie header received from the server
        }

        // Check for the CSRF token in the Set-Cookie headers
        val xsrfTokenHeader = originalResponse.headers("Set-Cookie")
            .find { it.startsWith("XSRF-TOKEN=") }

        // Extract and save the CSRF token if found
        xsrfTokenHeader?.let { header ->
            val csrfToken = header.substringAfter("XSRF-TOKEN=").substringBefore(";") // Extract CSRF token
            println("Extracted CSRF Token: $csrfToken")

            // Save the CSRF token in SharedPreferences for later use
            sharedPreferences.edit().putString("XSRF-TOKEN", csrfToken).apply()
        } ?: println("CSRF Token not found in Set-Cookie header.")

        // Retrieve the CSRF token from SharedPreferences for the new request
        val savedCsrfToken = sharedPreferences.getString("XSRF-TOKEN", null)

        // Create a new request with the X-XSRF-TOKEN if available
        val newRequest = originalRequest.newBuilder().apply {
            savedCsrfToken?.let { token ->
                addHeader("X-XSRF-TOKEN", token) // Add the X-XSRF-TOKEN header for the next request
            }
        }.build()

        // Close the original response to avoid resource leaks
        originalResponse.close()

        // Proceed with the new request containing the X-XSRF-TOKEN header
        return chain.proceed(newRequest)
    }
}


class DateHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        // Get the current time in the Africa/Tunis timezone
        val zonedDateTime = ZonedDateTime.now(ZoneId.of("Africa/Tunis"))

        // Format the date according to the HTTP standard format (RFC 1123) for the Date header
        val formatter = DateTimeFormatter.RFC_1123_DATE_TIME
        val formattedDate = zonedDateTime.withZoneSameInstant(ZoneId.of("GMT")).format(formatter)

        // Create a new request by adding the Date header
        val request = chain.request().newBuilder()
            .addHeader("Date", formattedDate) // Send Date in GMT
            .addHeader("X-Local-Date", zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"))) // Custom Local Date header
            .addHeader("X-Timezone", "Africa/Tunis") // Dynamically add the timezone header
            .build()

        return chain.proceed(request)
    }
}






