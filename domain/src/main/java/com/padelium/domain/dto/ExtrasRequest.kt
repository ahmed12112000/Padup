package com.padelium.domain.dto


import java.math.BigDecimal

data class ExtrasRequest(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val picture: String,
    val amount: BigDecimal,
    val currencyId: Long,
    val currencyName: Long,
    val isShared: Boolean
)
