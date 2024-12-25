package com.padelium.domain.dto

data class GetPaymentRequest(
    val bookingIds: List<bookingIds>,
    val couponIds: String,
    val numberOfPart:Int,
    val orderId: String,
    val privateExtrasIds:List<Long>,
    val sharedExtrasIds: List<Long>,
    val userIds: List<Long>

    )
