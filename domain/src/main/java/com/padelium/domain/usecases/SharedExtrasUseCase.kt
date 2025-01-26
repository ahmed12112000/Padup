package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.ISharedExtrasRepository
import javax.inject.Inject


class SharedExtrasUseCase @Inject constructor(private val sharedExtrasRepository: ISharedExtrasRepository) {

    suspend fun SharedExtras(): DataResult {
        return try {
            // Indicate loading state (optional: can be used if handling UI state)
            DataResult.Loading

            // Call the repository to fetch extras
            val sharedExtras = sharedExtrasRepository.SharedExtras()

            // Return the fetched data as a success result
            DataResult.Success(sharedExtras)
        } catch (ex: Exception) {
            // Catch any exceptions and return them as failure results
            DataResult.Failure(
                exception = ex,
                errorCode = null,
                errorMessage = ex.localizedMessage ?: "An error occurred during fetchExtras"
            )
        }
    }
}

