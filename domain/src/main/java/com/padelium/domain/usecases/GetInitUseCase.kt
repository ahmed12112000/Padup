package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.repositories.IGetInitRepository
import javax.inject.Inject

class GetInitUseCase @Inject constructor(private val getInitRepository: IGetInitRepository) {

    suspend fun  execute(key: String): DataResultBooking<GetInitResponse> {
        return try {
            // Fetching the initialization data using the repository
            val response = getInitRepository.getinit(key)

            // Checking if the response is successful
            if (response.isSuccessful) {
                val getInitResponse = response.body()
                if (getInitResponse != null) {
                    // Return a Success result with the fetched response body
                    DataResultBooking.Success(getInitResponse)
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
            // If an exception occurs, return Failure with the exception details
            DataResultBooking.Failure(e, null, e.localizedMessage ?: "An error occurred during the GetInit request")
        }
    }
}
