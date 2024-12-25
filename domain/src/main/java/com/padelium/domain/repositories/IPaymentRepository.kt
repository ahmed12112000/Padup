package com.padelium.domain.repositories

import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import retrofit2.Response

interface IPaymentRepository {
    suspend fun Payment (paymentRequest: PaymentRequest): Response<PaymentResponse?>

}
