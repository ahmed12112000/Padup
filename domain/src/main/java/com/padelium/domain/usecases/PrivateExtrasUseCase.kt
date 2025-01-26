package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IPrivateExtrasRepository
import javax.inject.Inject


class PrivateExtrasUseCase @Inject constructor(private val privateExtrasRepository: IPrivateExtrasRepository) {

    suspend fun PrivateExtras(): DataResult {
        return try {
            // Indicate loading state (optional: can be used if handling UI state)
            DataResult.Loading

            // Call the repository to fetch extras
            val privateExtras = privateExtrasRepository.PrivateExtras()

            // Return the fetched data as a success result
            DataResult.Success(privateExtras)
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

