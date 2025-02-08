package com.padelium.data.dto

data class PaymentPartBookingRequestDTO(

    val bookingId: Long,
    val id: Long,
    val orderId: String,
    val privateExtrasIds:List<Long?>,
)
