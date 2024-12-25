package com.padelium.domain.usecases

import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.repositories.IGetProfileRepository

class GetProfileUseCase(private val repository: IGetProfileRepository) {
    suspend fun execute(): GetProfileResponse {
        return repository.GetProfile()
    }
}

