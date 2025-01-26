package com.padelium.data.dto

data class PaymentGetAvoirRequestDTO(
    val amount: String,
    val paymentRef: String,
    val packId: String,
    val orderId: String,

    )
