package com.padelium.data.dto

import java.math.BigDecimal

data class PaymentGetAvoirRequestDTO(
    val amount: BigDecimal,
    val paymentRef: String,
    val packId: Long,
    val orderId: String,

    )
