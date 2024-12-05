package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.repositories.IPaymentRepository
import javax.inject.Inject





class PaymentUseCase @Inject constructor(private val paymentRepository: IPaymentRepository) {

    suspend fun Payment(paymentRequest: PaymentRequest): DataResult {
        return try {
            val response = paymentRepository.Payment(paymentRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "Payment result: ${response.code()}")
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
