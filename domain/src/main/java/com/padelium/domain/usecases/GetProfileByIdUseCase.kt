package com.padelium.domain.usecases

import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.repositories.IGetProfileByIdRepository
import javax.inject.Inject

class GetProfileByIdUseCase @Inject constructor(private val repository: IGetProfileByIdRepository) {

    suspend fun execute(id: Long): GetReservationIDResponse {
        return repository.GetProfileById(id)
    }
}
