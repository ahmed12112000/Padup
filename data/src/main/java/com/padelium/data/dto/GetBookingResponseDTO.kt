package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.HappyHours
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.EstablishmentDTO
import java.math.BigDecimal
import java.time.Instant

data class GetBookingResponseDTO(
    val EstablishmentDTO: List<EstablishmentDTO>,
    val amount: Double,
    val decimalNumber: Int,
    val currencySymbol: String,
    val facadeUrl: String,
    val openTime: Instant,
    val closeTime: Instant,
    val searchDate: String,
    val from: String,
    val to: String,
    val numberOfPlayer: Int,
    val description: String,
    val currencyId: Long,
    val mgAmount: BigDecimal,
    val totalFeed: Int,
    val moyFeed: Double,
    val bookingAnnulationDTOSet: List<Unit>,
    val secondAmount: BigDecimal,
    val secondAamount: BigDecimal,
    val key: String,
    val HappyHours: List<HappyHours>,

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
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val plannings: List<PlanningDTO>,
    val users: List<Long>,
    val isClient: Boolean = true,
    val secondReduction: Int,
    val aamount: BigDecimal,
    val EstablishmentPictureDTO: List<EstablishmentPictureDTO>,

    )
