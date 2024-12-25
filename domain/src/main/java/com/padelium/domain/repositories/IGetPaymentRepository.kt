package com.padelium.domain.repositories

import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.GetPaymentResponse
import retrofit2.Response


interface IGetPaymentRepository {
    suspend fun GetPayment (getPaymentRequest: GetPaymentRequest): Response<GetPaymentResponse>

}
