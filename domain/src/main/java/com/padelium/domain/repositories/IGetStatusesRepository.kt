package com.padelium.domain.repositories

import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.dto.GetStatusesResponse

interface IGetStatusesRepository {

    suspend fun GetStatuses(): List<GetStatusesResponse>

}