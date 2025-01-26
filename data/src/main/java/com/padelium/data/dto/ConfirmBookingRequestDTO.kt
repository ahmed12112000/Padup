package com.padelium.data.dto


import java.math.BigDecimal

data class ConfirmBookingRequestDTO(

    val amount: BigDecimal,
    val numberOfPart: Int,
    val payFromAvoir: Boolean,
    val privateExtrasIds: List<Long?>,
    val bookingIds: List<Long>,
    val buyerId: String,
    val couponIds: Map<Long, Long>,
    val sharedExtrasIds: List<Long?>,
    val status: Boolean,
    val token: String,
    val transactionId: String,
    val userIds: List<Long?>,
)

