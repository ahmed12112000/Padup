package com.padelium.domain.dto

data class PaymentGetAvoirRequest(
    val amount: String,
    val paymentRef: String,
    val packId: String,
    val orderId: String,

    )

