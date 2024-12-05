package com.padelium.domain.repositories

import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import retrofit2.Response

interface ISaveBookingRepository {
    suspend fun SaveBooking(saveBookingRequest: List<SaveBookingRequest>): Response<List<SaveBookingResponse>>
}
