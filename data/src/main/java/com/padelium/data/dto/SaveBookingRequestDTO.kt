package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentBasicDTO
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureBasicDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.PlanningBasicDTO
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.bookingAnnulationDTOSet
import com.padelium.domain.dto.happyHours
import com.padelium.domain.dto.happyHoursBasicDTO
import java.math.BigDecimal
import java.time.Instant

data class SaveBookingRequestt(
    val aamount: BigDecimal?,
    val amount: BigDecimal?,
    val amountfeeTrans: BigDecimal?,
    val bookingAnnulationDTOSet: Set<bookingAnnulationDTOSet?> = emptySet(),
    val end: String?,
    val establishmentDTO: EstablishmentDTO?,
    val from: String?,
    val numberOfPart: Int?,
    val payFromAvoir: Boolean?,
    val searchDate: String?,
    val start: String?,
    val to: String?,
    val userIds: List<Long?>,
    val plannings: List<PlanningDTO?>,
 //   val users: String,
    val privateExtrasIds: List<Long?>,
    val sharedExtrasIds: List<Long?>,



    // val usersIds:List<Long>,
    //
    // val sharedExtrasIds: List<Long>,
    //

    //
    //


)

data class TransformedBookingData(

    val establishmentDTO: EstablishmentDTO?,
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
    //val HappyHours: List<happyHours>,

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
    val rsamountfeeTrans: BigDecimal,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long,
    val plannings: List<PlanningDTO>,
    val users: List<Long>,
    val isClient: Boolean = true,
    val secondReduction: Int,
    val aamount: BigDecimal,
    val EstablishmentPictureDTO: List<EstablishmentPictureDTO>,
)




data class SaveBookingRequestDTO(
    val bookings: List<GetBookingResponseDTO>,

)
