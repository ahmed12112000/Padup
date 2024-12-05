package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.HappyHours
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.bookingAnnulationDTOSet
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class SaveBookingRequestDTO(

    val aamount: BigDecimal,
    val amount: BigDecimal,
    val amountfeeTrans: BigDecimal,
    val bookingAnnulationDTOSet: List<bookingAnnulationDTOSet>,
    val isClient:Boolean = true,
    val closeTime: Instant,
    val couponCode: String,
    val currencyId:Long,
    val currencySymbol:String,
    val decimalNumber: Int,
    val description: String,
    val end: String,
    val Establishment: List<EstablishmentDTO>,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val establishmentPictureDTO: List<EstablishmentPictureDTO>,
    val facadeUrl: String,
    val from: Instant,
    val HappyHours: List<HappyHours>,
    val mgAmount: BigDecimal,
    val moyFeed: Double,
    val numberOfPart:Int,
    val numberOfPlayer: Int,
    val openTime: Instant = Instant.now(), // Default value
    val payFromAvoir: Boolean,
    val plannings: List<PlanningDTO>,
    val privateExtrasIds:List<Long>,
    val ramountfeeTrans: BigDecimal,
    val reduction: Int,
    val reductionAmount: BigDecimal,
    val reductionSecondAmount: BigDecimal,
    val reductionaAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val searchDate: LocalDate,
    val secondAamount: BigDecimal,
    val secondAmount: BigDecimal,
    val secondReduction: Int,
    val sharedExtrasIds: List<Long>,
    val start: String,
    val to: String,
    val totalFeed: Int,
    val users: List<Long>,
    val usersIds:List<Long>,
    val withSecondPrice: Boolean,
)
