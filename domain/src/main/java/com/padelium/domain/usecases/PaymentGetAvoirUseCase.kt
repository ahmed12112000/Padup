package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import com.padelium.domain.repositories.IPaymentParCreditRepository
import com.padelium.domain.repositories.IPaymentPartBookingRepository
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


class PaymentPartBookingUseCase @Inject constructor(private val paymentGetAvoirRepository: IPaymentPartBookingRepository) {

    suspend fun PaymentPartBooking (paymentGetAvoirRequest: PaymentPartBookingRequest): DataResult {
        return try {
            val response = paymentGetAvoirRepository.PaymentPartBooking(paymentGetAvoirRequest)
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


class PaymentParCreditUseCase @Inject constructor(private val paymentParCreditRepository: IPaymentParCreditRepository) {

    suspend fun PaymentParCredit(paymentParCreditRequest: PaymentParCreditRequest): DataResult {
        return try {
            val response = paymentParCreditRepository.PaymentParCredit(paymentParCreditRequest)
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