package com.padelium.data.mappers

import com.padelium.data.dto.InitBookingRequestDTO
import com.padelium.data.dto.InitBookingResponseDTO
import com.padelium.data.dto.bookingDTO
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.dto.InitBookingResponse
import java.math.BigDecimal
import java.time.Instant
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
                from = response.from ?: Instant.now(), // Set default value if null
                to = response.to ?: Instant.now(),
                available = response.available,
                openTime = response.openTime ?: Instant.now(),
                closeTime = response.closeTime ?: Instant.now(),
                bookings = response.bookings?: emptyList(),
                availableBol = response.availableBol,
                dayWithBooking = response.dayWithBooking,
                fromStr = response.fromStr?: "",
                toStr = response.toStr?: "",
                price = response.price ?: BigDecimal.ZERO,
                feeTransaction = response.feeTransaction ?: BigDecimal.ZERO,
                reductionPrice = response.reductionPrice ?: BigDecimal.ZERO,
                rfeeTransaction = response.rfeeTransaction ?: BigDecimal.ZERO,
                currencySymbol = response.currencySymbol?: "",
                reductionPriceBol = response.reductionPriceBol,
                secondPrice = response.secondPrice,
                isHappyHours = response.isHappyHours,
                annulationDate = response.annulationDate?: ""
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


