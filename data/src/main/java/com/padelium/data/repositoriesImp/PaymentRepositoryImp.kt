package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.PaymentMapper
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.repositories.IPaymentRepository
import retrofit2.Response
import javax.inject.Inject


class PaymentRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentMapper,

    ) : IPaymentRepository {
    override suspend fun Payment(paymentRequest: PaymentRequest): Response<PaymentResponse> {
        return api.Payment(mapper.PaymentRequestToPaymentRequestDTO(paymentRequest))
    }


}

