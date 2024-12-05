package com.padelium.data.dto

data class PaymentResponseDTO(
    val payUrl: String,
    val paymentRef: String,
    val orderId: String,
    val formUrl: String,
    val errorCode: String,
    val errorMessage: String,
    val orderStatus: String,

    )

