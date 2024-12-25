package com.padelium.domain.usecases

import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.repositories.IGetReservationRepository


class GetReservationUseCase(private val repository: IGetReservationRepository) {
    suspend fun execute(): List<GetReservationResponse> {
        return repository.GetReservation()
    }
}

