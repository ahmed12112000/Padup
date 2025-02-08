package com.padelium.domain.repositories

import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import retrofit2.Response

interface IInitBookingRepository {

    suspend fun InitBooking (initBookingRequest: InitBookingRequest): Response<List<InitBookingResponse>>

}