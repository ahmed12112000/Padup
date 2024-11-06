package com.nevaDev.padeliummarhaba.repository.Booking

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BookingApi {

    @GET("api/establishments/search/get/booking")
    suspend fun getAvailableTimeSlots(
        @Header("Authorization") token: String,
        @Query("date") date: String
    ): Response<List<TimeSlot>>
}
