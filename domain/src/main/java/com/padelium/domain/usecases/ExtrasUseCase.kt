package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.repositories.IExtrasRepository
import java.math.BigDecimal
import javax.inject.Inject

class ExtrasUseCase @Inject constructor(private val extrasRepository: IExtrasRepository) {

    suspend fun Extras(): DataResult {
        return try {
            // Indicate loading state (optional: can be used if handling UI state)
            DataResult.Loading

            // Call the repository to fetch extras
            val extras = extrasRepository.Extras()

            // Return the fetched data as a success result
            DataResult.Success(extras)
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



