package com.padelium.domain.dto

import java.math.BigDecimal

data class GetPacksResponse(
    val id: Long,
    val name: String,
    val description: String,
    val code: String,
    val amount: BigDecimal,
    val isonline:Boolean,
    val title: String,
    val currency: currency,


    )
