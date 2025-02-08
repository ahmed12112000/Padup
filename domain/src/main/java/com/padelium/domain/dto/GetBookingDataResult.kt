package com.padelium.domain.dto

data class GetBookingDataResult(
    val amount: Double,
    val currencySymbol: String,
    val name: String,
    val fromStr: String,
    val toStr: String,
)
