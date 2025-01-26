package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IGetEmailRepository
import javax.inject.Inject


class GetEmailUseCase @Inject constructor(private val getEmailRepository: IGetEmailRepository) {

    suspend fun GetEmail(bookingIds: List<Long>): DataResult {
        return try {
            val response = getEmailRepository.GetEmail(bookingIds)

            if (response.isSuccessful) {
                val responseBody = response.body()

                if (responseBody != null) {
                    Log.e("TAG", "Received response: $responseBody")
                    // Return the response body (Long value) in a successful result
                    return DataResult.Success(responseBody)
                } else {
                    // Handle the case where the response body is null
                    DataResult.Failure(null, response.code(), "No data returned from server")
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            // Handle exceptions
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during balance fetching")
        }
    }
}

