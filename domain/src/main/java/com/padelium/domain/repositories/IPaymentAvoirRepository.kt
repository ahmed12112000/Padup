package com.padelium.domain.repositories

import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.dto.UserAvoirResponse
import retrofit2.Response


interface IPaymentAvoirRepository {
    suspend fun PaymentAvoir (userAvoirRequest: UserAvoirRequest): Response<UserAvoirResponse>

}
