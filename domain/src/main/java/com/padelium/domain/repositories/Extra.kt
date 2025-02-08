package com.padelium.domain.repositories

import java.math.BigDecimal

interface Extraa {
    val id: Long
    val name: String
    val code: String
    val description: String
    val picture: String
    val amount: BigDecimal
    val currencyId: Long
    val currencyName: String
    val isShared: Boolean
}