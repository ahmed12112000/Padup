package com.padelium.domain.usecase

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.repositories.IGetKeyRepository

import javax.inject.Inject
class KeyUseCase @Inject constructor(private val keyRepository: IGetKeyRepository) {

    suspend fun getReservationKey(fetchKeyRequest: FetchKeyRequest): DataResultBooking<FetchKeyResponse> {
        return try {
            // Fetching the reservation key using the repository
            val response = keyRepository.getReservationKey(fetchKeyRequest)

            // Checking if the response is successful
            if (response.isSuccessful) {
                val fetchKeyResponse = response.body()
                if (fetchKeyResponse != null) {
                    val key = fetchKeyResponse.key
                    // Return a Success result with the fetched response body
                    DataResultBooking.Success(fetchKeyResponse)
                } else {
                    // If the response body is null, return Failure
                    DataResultBooking.Failure(null, response.code(), "Empty response body")
                }
            } else {
                // If the response is not successful, return Failure with an error message
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            // If an exception occurs, return Failure with the exception details
            DataResultBooking.Failure(ex, null, ex.localizedMessage ?: "An error occurred during fetching key")
        }
    }
}


