package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentBasicDTO
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureBasicDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.PlanningBasicDTO
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.happyHoursBasicDTO
import java.math.BigDecimal
import java.time.Instant

data class SaveBookingRequestt(
    val aamount: BigDecimal?,
    val amount: BigDecimal?,
    val amountfeeTrans: BigDecimal?,
    val bookingAnnulationDTOSet: List<Unit>,
    val client:Boolean = true,
    val closeTime: String?,
    val couponCode: String?,
    val currencyId:Long?,
    val currencySymbol:String?,
    val decimalNumber: Int?,
    val description: String?,
    val end: String?,
    val Establishment: EstablishmentDTO,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPictureDTO: List<EstablishmentPictureDTO>,
    val facadeUrl: String?,
    val from: String?,
    val mgAmount: BigDecimal?,
    val moyFeed: Double?,
    val numberOfPart: Int?,
    val payFromAvoir: Boolean?,
    val privateExtrasIds: List<Long?>,
    val ramountfeeTrans: BigDecimal?,
    val reduction: Int?,
    val reductionAmount: BigDecimal?,
    val reductionSecondAmount: BigDecimal?,
    val reductionaAmount: BigDecimal?,
    val reductionaSecondAmount: BigDecimal?,
    val rsamountfeeTrans: BigDecimal?,
    val samountfeeTrans: BigDecimal?,
    val searchDate: String?,
    val secondAamount: BigDecimal?,
    val secondAmount: BigDecimal?,
    val secondReduction: Int?,
    val sharedExtrasIds: List<Long?>,
    val start: String?,
    val to: String?,
    val totalFeed: Int?,
    val withSecondPrice: Boolean?


    // val plannings: List<PlanningBasicDTO>,






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
