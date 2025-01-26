package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.PaymentGetAvoirMapper
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import retrofit2.Response
import javax.inject.Inject


class PaymentGetAvoirRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentGetAvoirMapper,

    ) : IPaymentGetAvoirRepository {
    override suspend fun PaymentGetAvoir (paymentGetAvoirRequuest: PaymentGetAvoirRequest): Response<Boolean> {
        return api.PaymentGetAvoir(mapper.PaymentGetAvoirRequestToPaymentGetAvoirRequestDTO(paymentGetAvoirRequuest))
    }


}
