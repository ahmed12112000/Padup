package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.repositories.IInitBookingRepository
import javax.inject.Inject
import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import retrofit2.HttpException

class InitBookingUseCase @Inject constructor(
    private val initBookingRepository: IInitBookingRepository
) {

    suspend fun InitBooking(initBookingRequest: InitBookingRequest): DataResult {
        return try {
            val response = initBookingRepository.InitBooking(initBookingRequest)

            if (response.isSuccessful) {
                Log.d("InitBookingResponse", "InitBooking successful: ${response.body()}")
                DataResult.Success(response.body() ?: "No data available")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: HttpException) {
            DataResult.Failure(ex, ex.code(), ex.localizedMessage ?: "An error occurred")
        } catch (ex: Exception) {
            DataResult.Failure(ex, 500, ex.localizedMessage ?: "An error occurred")
        }
    }
}



