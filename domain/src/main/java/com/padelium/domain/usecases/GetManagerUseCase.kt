package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IGetManagerRepository
import javax.inject.Inject


class GetManagerUseCase @Inject constructor(
    private val getManagerRepository: IGetManagerRepository
) {

    suspend fun GetManager(bookingIds: List<Long>): DataResult {
        return try {
            val response = getManagerRepository.GetManager(bookingIds)
            if (response.isSuccessful) {
                Log.e("TAG", "GetManager result: HTTP ${response.code()}")
                DataResult.Success(Unit) // Indicate success with no body
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("TAG", "Error in GetManager: $errorMessage")
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            Log.e("TAG", "Exception in GetManager: ${ex.localizedMessage}", ex)
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during the request")
        }
    }
}
