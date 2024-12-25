package com.padelium.data.dto

import com.padelium.domain.dto.EstablishmentBasicDTO
import com.padelium.domain.dto.EstablishmentPictureBasicDTO
import com.padelium.domain.dto.PlanningBasicDTO
import com.padelium.domain.dto.bookingExtrasDTO
import com.padelium.domain.dto.bookingUsersPaymentDTO
import com.padelium.domain.dto.happyHoursBasicDTO
import java.math.BigDecimal
import java.time.Instant

data class SaveBookingResponseDTO(
    val aamount: BigDecimal,
    val amount: BigDecimal,
    val amountfeeTrans: BigDecimal,
    val isClient:Boolean = true,
    val currencyId:Long,
    val currencySymbol:String,
    val decimalNumber: Int,
    val description: String,
    val end: String,

    val Establishment: EstablishmentBasicDTO,

    val HappyHours: List<happyHoursBasicDTO>,
    val establishmentPictureDTO: List<EstablishmentPictureBasicDTO>,

    val facadeUrl: String,


    val mgAmount: BigDecimal,
    val moyFeed: Double,
    val numberOfPart:Int,
    val payFromAvoir: Boolean,

    val plannings: List<PlanningBasicDTO>,


    val reductionSecondAmount: BigDecimal,
    val reductionaSecondAmount: BigDecimal,
    val rsamountfeeTrans: BigDecimal,
    val samountfeeTrans: BigDecimal,
    val searchDate: String,
    val secondAamount: BigDecimal,
    val secondAmount: BigDecimal,
    val secondReduction: Int,
    val start: String,
    val closeTime: String,
    val to: String,
    val from: String,
    val totalFeed: Int,

    val withSecondPrice: Boolean,

    // val usersIds:List<Long>,
    // val sharedExtrasIds: List<Long>,
    // val sharedExtrasIds: List<Long>,
    //  val privateExtrasIds:List<Long>,

    //val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    //  val bookingAnnulationDTOSet: List<bookingAnnulationDTOSet>,


)



/*
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Ha(
    val id: Int,
    val from: String?,
    val to: String?,
    val annulationDate: String?,
    val sellAmount: Double?,
    val purchaseAmount: Double?,
    val numberOfPlayer: Int?,
    val reference: String?,
    val description: String?,
    val isRefundable: Boolean?,
    val created: String?,
    val updated: String?,
    val createdBy: String?,
    val updatedBy: String?,
    val currencyFromId: Int?,
    val currencyToId: Int?,
    val bookingStatusId: Int?,
    val establishmentId: Int?,
    val userId: Int?,
    val userLogin: String?,
    val establishmentName: String?,
    val bookingStatusName: String?,
    val userEmail: String?,
    val currencyFromSymbol: String?,
    val gainFromManager: Double?,
    val bookingStatusCode: String?,
    val userPhone: String?,
    val cancelBook: Boolean?,
    val cancel: Boolean?,
    val isonline: Boolean?,
    val activityName: String?,
    val cityName: String?,
    val establishmentCode: String?,
    val localAmount: Double?,
    val reduction: Double?,
    val showCancel: Boolean?,
    val showfeedBack: Boolean?,
    val bookingDate: String?,
    val token: String?,
    val paymentError: Boolean?,
    val paymentprog: Boolean?,
    val amountToPay: Double?,
    val sobflousCode: String?,
    val couponId: Int?,
    val isCoupon: Boolean?,
    val couponValue: Double?,
    val couponCode: String?,
    val establishmentPacksId: Int?,
    val establishmentTypeCode: String?,
    val isConfirmed: Boolean?,
    val isFromEvent: Boolean?,
    val establishmentPacksFirstTitle: String?,
    val establishmentPacksSecondTitle: String?,
    val usersIds: List<Int>?,
    val bookingUsersPaymentListDTO: List<Any>?,
    val fromStr: String?,
    val toStr: String?,
    val fromStrTime: String?,
    val toStrTime: String?,
    val activePayment: Boolean?,
    val isWaitForPay: Boolean?,
    val bookingLabelId: Int?,
    val bookingLabelName: String?,
    val bookingLabelColor: String?,
    val sharedExtrasIds: List<Int>?,
    val privateExtrasIds: List<Int>?,
    val privateExtrasLocalIds: Map<String, Any>?,
    val userFirstName: String?,
    val userLastName: String?,
    val extras: List<Any>?,
    val numberOfPart: Int?,
    val createdStr: String?
)

 */
