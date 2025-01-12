package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.UserAvoirPayRequest
import com.padelium.domain.repositories.IPaymentPayAvoirrepository
import java.math.BigDecimal
import javax.inject.Inject

class PaymentPayAvoirUseCase @Inject constructor(
    private val paymentPayAvoirrepository: IPaymentPayAvoirrepository
) {

    suspend fun PaymentPayAvoir(amount: BigDecimal): DataResult {
        Log.d("UseCase", "PaymentPayAvoir called with amount: $amount")
        return try {
            val response = paymentPayAvoirrepository.PaymentPayAvoir(amount)
            if (response.isSuccessful) {
                Log.d("UseCase", "PaymentPayAvoir success: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("UseCase", "PaymentPayAvoir failure: $errorMessage")
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            Log.e("UseCase", "PaymentPayAvoir exception: ${ex.localizedMessage}")
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during payment processing")
        }
    }
}



