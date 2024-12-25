package com.padelium.domain.repositories

import com.padelium.domain.dto.GetReservationResponse


interface IGetReservationRepository {

        suspend fun GetReservation(): List<GetReservationResponse>

}
