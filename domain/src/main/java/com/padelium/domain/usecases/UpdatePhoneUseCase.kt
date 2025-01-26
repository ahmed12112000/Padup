package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IUpdatePhoneRepository
import okhttp3.RequestBody
import javax.inject.Inject


class UpdatePhoneUseCase @Inject constructor(
    private val updatePhoneRepository: IUpdatePhoneRepository
) {

    suspend fun UpdatePhone(Phone: RequestBody): DataResult {
        return try {
            val response = updatePhoneRepository.UpdatePhone(Phone)

            if (response.isSuccessful) {
                Log.e("TAG", "GetManager result: HTTP ${response.code()}")

                val body = response.body()
                if (body != null) {
                    DataResult.Success(body)
                } else {
                    Log.e("TAG", "Response body is null")
                    DataResult.Failure(null, response.code(), "Response body is null")
                }
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
