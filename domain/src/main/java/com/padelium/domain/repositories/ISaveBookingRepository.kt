package com.padelium.domain.repositories

import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import retrofit2.Response

interface ISaveBookingRepository {
    suspend fun SaveBooking(saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>>
}
