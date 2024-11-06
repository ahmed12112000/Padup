package com.padelium.data.dto

import org.threeten.bp.Instant
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class GetBookingList(
    val Establishment: List<EstablishmentDTO> = emptyList(),


    val establishmentPictureDTO: List<EstablishmentPictureDTO>,
    val amount: Double,
    val decimalNumber: Int,
    val currencySymbol: String,
    val facadeUrl: String,
    val openTime: Instant,
    val closeTime: Instant,
    val searchDate: LocalDate,
    val from: Instant,
    val to: Instant,
    val numberOfPlayer: Int,
    val description: String,
    val currencyId: Long,
    val mgAmount: BigDecimal,
    val totalFeed: Int,
    val moyFeed: Double,
    val bookingAnnulationDTOSet: List<Unit>,
    val secondAmount: BigDecimal,
    val secondAamount: BigDecimal,

    val HappyHours: List<HappyHours> = emptyList(),

    val withSecondPrice: Boolean,
    val reductionAmount: BigDecimal,
    val reductionSecondAmount: BigDecimal,
    val payFromAvoir: Boolean,
    val reduction: Int,
    val reductionaAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val start: String,
    val end: String,
    val amountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val ramountfeeTrans: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val couponCode: String,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>  = emptyList(),
    val establishmentPacksId: Long,
    val plannings: List<PlanningDTO> = emptyList(),
    val users: List<Long>,
    val isClient: Boolean = true,
    val secondReduction: Int,
    val aamount: BigDecimal,


    ) : Serializable

