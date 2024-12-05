package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.repositories.ISaveBookingRepository
import javax.inject.Inject




class SaveBookingUseCase @Inject constructor(private val saveBookingRepository: ISaveBookingRepository) {

    suspend fun SaveBooking(saveBookingRequest: List<SaveBookingRequest>): DataResult {
        return try {
            val response = saveBookingRepository.SaveBooking(saveBookingRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "SaveBooking result: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during login")
        }
    }
}
