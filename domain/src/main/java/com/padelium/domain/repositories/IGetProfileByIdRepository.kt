package com.padelium.domain.repositories

import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse

interface IGetProfileByIdRepository {

    suspend fun GetProfileById(id: Long): GetReservationIDResponse

}