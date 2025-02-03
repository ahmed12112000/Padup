package com.padelium.domain.usecases

import com.padelium.domain.dto.GetStatusesResponse
import com.padelium.domain.repositories.IGetStatusesRepository


class GetStatusesUseCase(private val repository: IGetStatusesRepository) {
    suspend fun execute(): List<GetStatusesResponse> {
        return repository.GetStatuses()
    }
}

