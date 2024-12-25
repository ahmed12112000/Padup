package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.repositories.ISaveBookingRepository
import javax.inject.Inject

class SaveBookingUseCase @Inject constructor(
    private val saveBookingRepository: ISaveBookingRepository
) {

    suspend fun SaveBooking(getBookingResponses: List<GetBookingResponse>): DataResult {
        return try {
            // Call the repository to save the booking
            val response = saveBookingRepository.SaveBooking(getBookingResponses)

            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null && responseBody.isNotEmpty()) {
                    Log.d("SaveBookingUseCase", "SaveBooking successful: ${response.code()}")
                    DataResult.Success(responseBody) // Return the list of SaveBookingResponse
                } else {
                    Log.d("SaveBookingUseCase", "Empty response body")
                    DataResult.Success(emptyList<SaveBookingResponse>()) // Return an empty success result
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("SaveBookingUseCase", "Error response: $errorMessage")
                DataResult.Failure(
                    exception = null,
                    errorCode = response.code(),
                    errorMessage = errorMessage
                )
            }
        } catch (ex: Exception) {
            Log.e("SaveBookingUseCase", "Exception occurred during booking", ex)
            DataResult.Failure(
                exception = ex,
                errorCode = null,
                errorMessage = ex.localizedMessage ?: "An error occurred during booking"
            )
        }
    }
}






