package com.padelium.data.mappers

import com.padelium.data.dto.PaymentRequestDTO
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import javax.inject.Inject


class PaymentMapper @Inject constructor() {

    fun PaymentRequestToPaymentRequestDTO(paymentRequest: PaymentRequest): PaymentRequestDTO {
        return PaymentRequestDTO(
            amount = paymentRequest.amount,
            currency = paymentRequest.currency,
            orderId = paymentRequest.orderId
        )
    }

    fun PaymentResponseToPaymentResponseDTO(paymentResponse: PaymentResponse): PaymentResponse {
        return PaymentResponse(
            payUrl = paymentResponse.payUrl ?: "",
            paymentRef = paymentResponse.paymentRef ?: "",
            orderId = paymentResponse.orderId ?: "",
            formUrl = paymentResponse.formUrl ?: "",
            errorCode = paymentResponse.errorCode ?: "",
            errorMessage = paymentResponse.errorMessage ?: "",
            orderStatus = paymentResponse.orderStatus ?: ""
        )
    }
}

