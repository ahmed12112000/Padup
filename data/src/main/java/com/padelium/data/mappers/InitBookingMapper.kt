package com.padelium.data.mappers

import com.padelium.data.dto.InitBookingRequestDTO
import com.padelium.data.dto.InitBookingResponseDTO
import com.padelium.data.dto.bookingDTO
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject

class InitBookingMapper @Inject constructor() {

    fun initBookingRequestToInitBookingRequestDto(initBookingRequest: InitBookingRequest): InitBookingRequestDTO {
        return InitBookingRequestDTO(
            key = initBookingRequest.key
        )
    }

    fun initBookingResponseToInitBookingResponseDTO(
        initBookingResponse: List<InitBookingResponse>
    ): List<InitBookingResponseDTO> {
        return initBookingResponse.map { response ->
            InitBookingResponseDTO(
                establishmentPictureDTO = response.establishmentPictureDTO ?: emptyList(),
                amount = response.amount ?: BigDecimal.ZERO,
                decimalNumber = response.decimalNumber ?: 0,
                currencySymbol = response.currencySymbol ?: "",
                facadeUrl = response.facadeUrl ?: "",
                openTime = response.openTime ?: Instant.now(),
                closeTime = response.closeTime ?: Instant.now(),
                searchDate = response.searchDate ?: "",
                from = response.from ?:"",
                to = response.to ?:"",
                numberOfPlayer = response.numberOfPlayer ?: 0,
                description = response.description ?: "",
                currencyId = response.currencyId ?: 0L,
                mgAmount = response.mgAmount ?: BigDecimal.ZERO,
                totalFeed = response.totalFeed ?: 0,
                moyFeed = response.moyFeed ?: 0.0,
                bookingAnnulationDTOSet = response.bookingAnnulationDTOSet ?: emptyList(),
                secondAmount = response.secondAmount ?: BigDecimal.ZERO,
                secondAamount = response.secondAamount ?: BigDecimal.ZERO,
                happyHours = response.happyHours ?: "" ,
                withSecondPrice = response.withSecondPrice ?: false,
                reductionAmount = response.reductionAmount ?: BigDecimal.ZERO,
                reductionSecondAmount = response.reductionSecondAmount ?: BigDecimal.ZERO,
                payFromAvoir = response.payFromAvoir ?: false,
                reduction = response.reduction ?: BigDecimal.ZERO,
                reductionaAmount = response.reductionaAmount ?: BigDecimal.ZERO,
                reductionaSecondAmount = response.reductionaSecondAmount ?: BigDecimal.ZERO,
                start = response.start ?: "",
                end = response.end ?: "",
                amountfeeTrans = response.amountfeeTrans ?: BigDecimal.ZERO,
                samountfeeTrans = response.samountfeeTrans ?: BigDecimal.ZERO,
                ramountfeeTrans = response.ramountfeeTrans ?: BigDecimal.ZERO,
                rsamountfeeTrans = response.rsamountfeeTrans ?: BigDecimal.ZERO,
                couponCode = response.couponCode ?: "",
                establishmentPacksDTO = response.establishmentPacksDTO ?: emptyList(),
                establishmentPacksId = response.establishmentPacksId ?: 0L,
                plannings = response.plannings ?: emptyList(),
                users = response.users ?: emptyList(),
                Client = response.Client ?: false,
                secondReduction = response.secondReduction ?: BigDecimal.ZERO,
                aamount = response.aamount ?: BigDecimal.ZERO
            )
        }
    }
}

/*
   fun initBookingRequestToInitBookingRequestDto(initBookingRequest: InitBookingRequest): InitBookingRequestDTO {
        return InitBookingRequestDTO(
            key = initBookingRequest.key

        )
    }
 */

/*
 fun initBookingResponseToInitBookingResponseDTO(
        initBookingResponse: List<InitBookingResponse>
    ): List<InitBookingResponseDTO> {

        return initBookingResponse.map { response ->
            InitBookingResponseDTO(

// Extension function to map Booking to bookingDTO
object BookingMapper {
    fun toBookingDTO(booking: Booking): bookingDTO {
        return bookingDTO(
            id = booking.id,
            from = booking.from,
            to = booking.to,
            annulationDate = booking.annulationDate,
            sellAmount = booking.sellAmount,
            purchaseAmount = booking.purchaseAmount,
            numberOfPlayer = booking.numberOfPlayer,
            reference = booking.reference,
            description = booking.description,
            isRefundable = booking.isRefundable,
            created = booking.created,
            updated = booking.updated,
            createdBy = booking.createdBy,
            updatedBy = booking.updatedBy,
            currencyFromId = booking.currencyFromId,
            currencyToId = booking.currencyToId,
            bookingStatusId = booking.bookingStatusId,
            establishmentId = booking.establishmentId,
            userId = booking.userId,
            userLogin = booking.userLogin,
            establishmentName = booking.establishmentName,
            bookingStatusCode = booking.bookingStatusCode,
            userPhone = booking.userPhone,
            cancelBook = booking.cancelBook,
            cancel = booking.cancel,
            isonline = booking.isonline,
            activityName = booking.activityName,
            cityName = booking.cityName,
            establishmentCode = booking.establishmentCode,
            localAmount = booking.localAmount,
            reduction = booking.reduction,
            showcancel = booking.showcancel,
            showfeedBack = booking.showfeedBack,
            bookingDate = booking.bookingDate,
            token = booking.token,
            paymentError = booking.paymentError,
            paymentprog = booking.paymentprog,
            amountToPay = booking.amountToPay,
            sobflousCode = booking.sobflousCode,
            couponId = booking.couponId,
            isCoupon = booking.isCoupon,
            couponValue = booking.couponValue,
            couponCode = booking.couponCode,
            establishmentPacksId = booking.establishmentPacksId,
            establishmentTypeCode = booking.establishmentTypeCode,
            isConfirmed = booking.isConfirmed,
            isFromEvent = booking.isFromEvent,
            establishmentPacksFirstTitle = booking.establishmentPacksFirstTitle,
            establishmentPacksSecondTitle = booking.establishmentPacksSecondTitle,
            usersIds = booking.usersIds,
            bookingUsersPaymentListDTO = booking.bookingUsersPaymentList.map { it.toBookingUsersPaymentDTO() },
            fromStr = booking.fromStr,
            toStr = booking.toStr,
            fromStrTime = booking.fromStrTime,
            toStrTime = booking.toStrTime,
            activePayment = booking.activePayment,
            isWaitForPay = booking.isWaitForPay,
            bookingLabelId = booking.bookingLabelId,
            bookingLabelName = booking.bookingLabelName,
            bookingLabelColors = booking.bookingLabelColors,
            sharedExtrasIds = booking.sharedExtrasIds,
            privateExtrasIds = booking.privateExtrasIds,
            userFirstName = booking.userFirstName,
            userLastName = booking.userLastName,
            extras = booking.extras.map { it.toBookingExtrasDTO() },
            numberOfPart = booking.numberOfPart,
            createdStr = booking.createdStr,
            privateExtrasLocalIds = booking.privateExtrasLocalIds
        )
    }
}
*/


