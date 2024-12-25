package com.padelium.data.dto

import java.math.BigDecimal

data class ExtrasResponseDTO(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val picture: String,
    val amount: BigDecimal,
    val currencyId: Long,
    val currencyName: String,
    val isShared: Boolean
)
