package com.padelium.domain.repositories


import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import retrofit2.Response

interface IPaymentGetAvoirRepository {
    suspend fun PaymentGetAvoir (paymentGetAvoirRequestDTO: PaymentGetAvoirRequest): Response<Boolean>

}

interface IPaymentPartBookingRepository {
    suspend fun PaymentPartBooking (paymentPartBookingRequest: PaymentPartBookingRequest): Response<Boolean>

}

interface IPaymentParCreditRepository {
    suspend fun PaymentParCredit (paymentParCreditRequest: PaymentParCreditRequest): Response<Boolean>

}