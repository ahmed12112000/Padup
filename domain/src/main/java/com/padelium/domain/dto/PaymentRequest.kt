package com.padelium.domain.dto

data class PaymentRequest(
    val amount: String,
    val currency: String,
    val orderId: String,

)
