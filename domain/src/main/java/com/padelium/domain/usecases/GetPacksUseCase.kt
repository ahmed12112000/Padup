package com.padelium.domain.usecases

import com.padelium.domain.dto.GetPacksResponse
import com.padelium.domain.repositories.IGetPacksRepository


class GetPacksUseCase(private val repository: IGetPacksRepository) {
    suspend fun execute(): List<GetPacksResponse> {
        return repository.GetPacks()
    }
}
