package com.padelium.data.dto

import org.threeten.bp.Instant
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate

data class BookingResponse(

    val aamount: BigDecimal,
    val amount: BigDecimal,
    val amountfeeTrans: BigDecimal,
    val bookingAnnulationDTOSet: List<bookingAnnulationDTO>,
    val isClient:Boolean,
    val closeTime: Instant,
    val couponCode: String,
    val currencyId: Long,
    val currencySymbol: String,
    val decimalNumber: Int,
    val description: String,
    val end: String,
    val establishmentDTO: List<EstablishmentDTO>,
    val EstablishmentPacksDTO: Set<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val establishmentPictureDTO: Set<EstablishmentPictureDTO>,
    val facadeUrl: String,
    val from: Instant,
    val HappyHours: List<HappyHours>,
    val mgAmount: BigDecimal,
    val moyFeed: Double,
    val numberOfPlayer: Int,
    val openTime: Instant,
    val payFromAvoir: Boolean,
    val PlanningDTO:List<PlanningDTO>,
    val ramountfeeTrans: BigDecimal,
    val reductionPrice: BigDecimal,
    val reductionAmount: BigDecimal,
    val reductionSecondAmount: BigDecimal,
    val reductionaAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val searchDate: LocalDate,
    val SecondAamount: BigDecimal,
    val secondAmount: BigDecimal,
    val secondReduction: Int,
    val start: String,
    val to: Instant,
    val totalFeed: Int,
    val users: List<Long>,
    val withSecondPrice: Boolean,
    val establishmentSearchDTO:List<establishmentSearchDTO>,
    val contactTypeDTO:List<contactTypeDTO>,
    val bookingExtrasDTO:List<bookingExtrasDTO>,
    val bookingUsersPaymentDTO:List<bookingUsersPaymentDTO>,
    val bookingDTO:List<bookingDTO>,
    val happyHoursDTO:List<happyHoursDTO>,
    val bookingAnnulationDTO:List<bookingAnnulationDTO>,
    val packsFeatureDTO: MutableList<packsFeatureDTO> = ArrayList(),
    val contactDTO:List<contactDTO>,
    val establishmentFeatureDTO:List<establishmentFeatureDTO>

) : Serializable