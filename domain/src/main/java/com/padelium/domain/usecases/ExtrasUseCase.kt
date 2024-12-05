package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.repositories.IExtrasRepository
import javax.inject.Inject


class ExtrasUseCase @Inject constructor(private val extrasRepository: IExtrasRepository) {

    suspend fun Extras(extrasRequest: List<ExtrasRequest>): DataResult {
        return try {
            val response = extrasRepository.Extras(extrasRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "Extras result: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during Extras")
        }
    }
}
