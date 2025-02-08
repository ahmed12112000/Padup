package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.repositories.IInitBookingRepository
import javax.inject.Inject

class InitBookingUseCase @Inject constructor(
    private val initBookingRepository: IInitBookingRepository
) {

    suspend fun execute(key: String): DataResultBooking<List<InitBookingResponse>> {
        // Check if the provided key is null or empty
        if (key.isBlank()) {
            return DataResultBooking.Failure(null, null, "Key is null or empty")
        }

        return try {
            // Create the request object using the provided key
            val request = InitBookingRequest(key.trim())

            // Fetching the initialization data using the repository
            val response = initBookingRepository.InitBooking(request)

            // Checking if the response is successful
            if (response.isSuccessful) {
                val initBookingResponse = response.body()
                if (!initBookingResponse.isNullOrEmpty()) {
                    // Return a Success result with the fetched response body
                    DataResultBooking.Success(initBookingResponse)
                } else {
                    // If the response body is null or empty, return Failure
                    DataResultBooking.Failure(
                        null,
                        response.code(),
                        "Empty response body"
                    )
                }
            } else {
                // If the response is not successful, return Failure with an error message
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (e: Exception) {
            // If an exception occurs, return Failure with the exception details
            DataResultBooking.Failure(e, null, e.localizedMessage ?: "An error occurred during the booking request")
        }
    }
}




