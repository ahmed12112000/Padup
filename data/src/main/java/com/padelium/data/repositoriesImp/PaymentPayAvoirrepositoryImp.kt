package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.PaymentPayAvoirMapper
import com.padelium.domain.dto.UserAvoirPayRequest
import com.padelium.domain.dto.UserAvoirPayResponse
import com.padelium.domain.repositories.IPaymentPayAvoirrepository
import retrofit2.Response
import java.math.BigDecimal
import javax.inject.Inject


class PaymentPayAvoirrepositoryImp @Inject constructor(
    private val api: PadeliumApi // Updated naming for clarity
) : IPaymentPayAvoirrepository {
    override suspend fun PaymentPayAvoir(amount: BigDecimal): Response<UserAvoirPayResponse> {
        // Directly pass the amount to the API endpoint
        return api.PaymentPayAvoir(amount)
    }
}
