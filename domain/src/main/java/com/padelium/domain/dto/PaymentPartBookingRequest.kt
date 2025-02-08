package com.padelium.domain.dto

data class PaymentPartBookingRequest(

    val bookingId: Long,
    val id: Long,
    val orderId: String,
    val privateExtrasIds:List<Long?>,
)
