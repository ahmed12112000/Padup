package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.repositories.IGetBookingRepository
import javax.inject.Inject

class GetBookingUseCase @Inject constructor(private val getBookingRepository: IGetBookingRepository) {

    suspend fun execute(key: String): DataResultBooking<List<GetBookingResponse>> {
        return try {
            // Fetching the booking data using the repository
            val response = getBookingRepository.GetBooking(key)

            // Checking if the response is successful
            if (response.isSuccessful) {
                val getBookingResponse = response.body()
                if (getBookingResponse != null) {
                    // Return Success with the response body
                    DataResultBooking.Success(getBookingResponse)
                } else {
                    // If the response body is null, return Failure
                    DataResultBooking.Failure(null, response.code(), "Empty response body")
                }
            } else {
                // If the response is not successful, return Failure with an error message
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResultBooking.Failure(null, response.code(), errorMessage)
            }
        } catch (e: Exception) {
            // If an exception occurs, return Failure with exception details
            DataResultBooking.Failure(e, null, e.localizedMessage ?: "An error occurred during the GetBooking request")
        }
    }
}
