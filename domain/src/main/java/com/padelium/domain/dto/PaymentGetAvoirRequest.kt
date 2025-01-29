package com.padelium.domain.dto

import java.math.BigDecimal

data class PaymentGetAvoirRequest(
    val amount: BigDecimal,
    val paymentRef: String,
    val packId: Long,
    val orderId: String,

    )

