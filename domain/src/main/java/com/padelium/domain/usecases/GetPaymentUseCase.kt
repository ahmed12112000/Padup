package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.repositories.IGetPaymentRepository
import javax.inject.Inject


class GetPaymentUseCase @Inject constructor(private val getPaymentRepository: IGetPaymentRepository) {

    suspend fun GetPayment(getPaymentRequest: GetPaymentRequest): DataResult {
        return try {
            val response = getPaymentRepository.GetPayment(getPaymentRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "GetPayment result: ${response.code()}")
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
