package com.padelium.data.dto

data class GetBookingDataResultDto(
    val amount: Double,
    val currencySymbol: String,
    val name: String,
    val fromStr: String,
    val toStr: String,
    )
