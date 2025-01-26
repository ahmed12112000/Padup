package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import javax.inject.Inject


class PaymentGetAvoirUseCase @Inject constructor(private val paymentGetAvoirRepository: IPaymentGetAvoirRepository) {

    suspend fun PaymentGetAvoir (paymentGetAvoirRequest: PaymentGetAvoirRequest): DataResult {
        return try {
            val response = paymentGetAvoirRepository.PaymentGetAvoir(paymentGetAvoirRequest)
            if (response.isSuccessful) {
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
