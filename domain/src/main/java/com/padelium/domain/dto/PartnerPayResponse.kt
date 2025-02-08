package com.padelium.domain.dto

import java.math.BigDecimal
import android.os.Parcelable
import java.io.Serializable

data class PartnerPayResponse(
    val id: Long = 0L,
    val code: String? = null,
    val isactive: Boolean = false,
    val description: String? = null,
    val amount: BigDecimal = BigDecimal.ZERO,
    val amountstr: String = "0.00",
    val bookingId: Long = 0L,
    val bookingDate: String = "",
    val bookingDateStr: String = "",
    val bookingEstablishmentName: String = "",
    val bookingCreatedFirstName: String = "",
    val bookingCreatedLastName: String = "",
    val userId: Long = 0L,
    val userLogin: String = "",
    val userEmail: String = "",
    val bookingUsersPaymentStatusId: Long = 0L,
    val userLastName: String = "",
    val userFirstName: String = "",
    val bookingUsersPaymentStatusName: String = "",
    val paymentMode: String? = null,
    val paymentStatus: Boolean = false,
    val bookingUsersPaymentStatusCode: String = ""
) : Serializable