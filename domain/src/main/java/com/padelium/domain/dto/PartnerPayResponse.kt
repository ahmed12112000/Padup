package com.padelium.domain.dto

import java.math.BigDecimal

data class PartnerPayResponse(
    val id : Long,
    val code: String,
    val isactive:Boolean,
    val description:String,
    val amount: BigDecimal,
    val amountstr:BigDecimal,
    val bookingId: Long,
    val bookingDate:String,
    val bookingDateStr:String,
    val bookingEstablishmentName:String,
    val bookingCreatedFirstName:String,
    val bookingCreatedLastName:String,
    val userId: Long,
    val userLogin: String,
    val userEmail: String,
    val BookingUsersPaymentStatusId:Long,
    val userLastName:String,
    val userFirstName:String,
    val BookingUsersPaymentStatusName:String,
    val paymentMode:String,
    val paymentStatus:Boolean ,
    val bookingUsersPaymentStatusCode:String


    )
