package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.repositories.IPaymentAvoirRepository
import javax.inject.Inject


class PaymentAvoirUseCase @Inject constructor(private val paymentAvoirRepository: IPaymentAvoirRepository) {

    suspend fun Payment(userAvoirRequest: UserAvoirRequest): DataResult {
        return try {
            val response = paymentAvoirRepository.PaymentAvoir(userAvoirRequest)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Log.d("PaymentUseCase", "Payment successful: ${response.code()}")
                    DataResult.Success(responseBody)
                } else {
                    Log.d("PaymentUseCase", "Empty response body")
                    DataResult.Success(Unit) // Use Unit as a placeholder
                }
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            Log.e("PaymentUseCase", "Exception occurred during payment", ex)
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment")
        }
    }


}
