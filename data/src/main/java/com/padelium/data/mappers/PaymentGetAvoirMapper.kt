package com.padelium.data.mappers

import com.padelium.data.dto.PaymentGetAvoirRequestDTO
import com.padelium.domain.dto.PaymentGetAvoirRequest
import javax.inject.Inject


class PaymentGetAvoirMapper @Inject constructor() {

    fun PaymentGetAvoirRequestToPaymentGetAvoirRequestDTO(paymentGetAvoirRequest: PaymentGetAvoirRequest): PaymentGetAvoirRequestDTO {
        return PaymentGetAvoirRequestDTO(
            amount = paymentGetAvoirRequest.amount,
            paymentRef = paymentGetAvoirRequest.paymentRef,
            packId = paymentGetAvoirRequest.packId,
            orderId = paymentGetAvoirRequest.orderId,

            )
    }
}




