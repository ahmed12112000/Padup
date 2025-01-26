package com.padelium.domain.repositories


import com.padelium.domain.dto.PaymentGetAvoirRequest
import retrofit2.Response

interface IPaymentGetAvoirRepository {
    suspend fun PaymentGetAvoir (paymentGetAvoirRequestDTO: PaymentGetAvoirRequest): Response<Boolean>

}