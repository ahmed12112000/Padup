package com.padelium.data.dto

import java.math.BigDecimal

data class CreditPayResponseDTO(

    val id: Long,
    val amount: BigDecimal,
    val created: String,
    val updated: String,
    val createdBy: Long,
    val updatedBy: Long,
    val description:String,
    val userId:Long,
    val userLogin:String,
    val userFirstName:String,
    val userLastName:String,
    val bookingId: Long,
    val bookingReference:String,
    val bookingSellAmount:String,
    val bookingCreation:String,
    val bookingEstablishmentName:String,
    val createdByFirstName:String,
    val createdByLastName:String,
    val createdByLogin:String,
    val token:String,
    val transactionId:String,
    val userAvoirTypeId:Long,
    val userAvoirTypeName:String,
    val createdStr:String,
    val buyerId:String,
    val packId:String,
    val bookingEstablishmentCode:String,
)
