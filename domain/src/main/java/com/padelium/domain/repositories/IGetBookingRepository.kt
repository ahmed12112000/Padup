package com.padelium.domain.repositories

import com.padelium.domain.dto.GetBookingResponse
import retrofit2.Response

interface IGetBookingRepository {
    suspend fun GetBooking (key: String): Response<List<GetBookingResponse>>
}