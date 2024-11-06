package com.nevaDev.padeliummarhaba.repository

import android.content.SharedPreferences
import com.nevaDev.padeliummarhaba.models.BookingRequest
import com.nevaDev.padeliummarhaba.retrofit.CsrfInterceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class BookingRequest(
    val key: String,
    val date: String
)

data class ReservationOption(
    val name: String,
    val time: String,
    val price: String,
    val duration: String
)

data class ReservationKeyResponse(
    val key: String
)
data class TimeSlotResponse(
    val availableSlots: List<String>
)

interface ReservationApiService {

        @POST("api/establishments/search/init")
        suspend fun getReservationKey(@Body request: Map<String, String>): ReservationKeyResponse

    @POST("api/establishments/search/init/booking")
    suspend fun bookReservation(@Body request: BookingRequest): Response<Void>

    @POST("api/establishments/search/get/booking")
    suspend fun getBookingTimeSlots(@Body request: BookingRequest): TimeSlotResponse // Fetch time slots
}

// Singleton object to create a Retrofit instance
object RetrofitInstance {
    private var retrofit: Retrofit? = null

    fun init(sharedPreferences: SharedPreferences) {
        if (retrofit == null) {
            val client = OkHttpClient.Builder()
                .addInterceptor(CsrfInterceptor(sharedPreferences))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl("http://141.94.246.248/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    // Create a new API service instance when needed
    fun create(): ReservationApiService {
        return retrofit?.create(ReservationApiService::class.java)
            ?: throw IllegalStateException("Retrofit has not been initialized.")
    }
}



