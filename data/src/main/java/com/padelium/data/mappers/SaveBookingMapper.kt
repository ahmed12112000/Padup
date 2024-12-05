package com.padelium.data.mappers

import com.padelium.data.dto.SaveBookingRequestDTO
import com.padelium.data.dto.SaveBookingResponseDTO
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject


class SaveBookingMapper @Inject constructor(){

    fun SaveBookingRequestToSaveBookingRequestDTO(saveBookingRequest: SaveBookingRequest): SaveBookingRequestDTO {
        return SaveBookingRequestDTO(
            aamount = saveBookingRequest.aamount,
            amount = saveBookingRequest.amount,
            amountfeeTrans = saveBookingRequest.amountfeeTrans,
            bookingAnnulationDTOSet = saveBookingRequest.bookingAnnulationDTOSet,
            isClient = saveBookingRequest.isClient,
            closeTime = saveBookingRequest.closeTime,
            couponCode = saveBookingRequest.couponCode,
            currencyId = saveBookingRequest.currencyId,
            currencySymbol = saveBookingRequest.currencySymbol,
            decimalNumber = saveBookingRequest.decimalNumber,
            description = saveBookingRequest.description,
            end = saveBookingRequest.end,
            Establishment = saveBookingRequest.Establishment,
            establishmentPacksDTO = saveBookingRequest.establishmentPacksDTO,
            establishmentPacksId = saveBookingRequest.establishmentPacksId,
            establishmentPictureDTO = saveBookingRequest.establishmentPictureDTO,
            facadeUrl = saveBookingRequest.facadeUrl,
            from = saveBookingRequest.from,
            HappyHours = saveBookingRequest.HappyHours,
            mgAmount = saveBookingRequest.mgAmount,
            moyFeed = saveBookingRequest.moyFeed,
            numberOfPart = saveBookingRequest.numberOfPart,
            numberOfPlayer = saveBookingRequest.numberOfPlayer,
            openTime = saveBookingRequest.openTime,
            payFromAvoir = saveBookingRequest.payFromAvoir,
            plannings = saveBookingRequest.plannings,
            privateExtrasIds = saveBookingRequest.privateExtrasIds,
            ramountfeeTrans = saveBookingRequest.ramountfeeTrans,
            reduction = saveBookingRequest.reduction,
            reductionAmount = saveBookingRequest.reductionAmount,
            reductionSecondAmount = saveBookingRequest.reductionSecondAmount,
            reductionaAmount = saveBookingRequest.reductionaAmount,
            reductionaSecondAmount = saveBookingRequest.reductionaSecondAmount,
            rsamountfeeTrans = saveBookingRequest.rsamountfeeTrans,
            samountfeeTrans = saveBookingRequest.samountfeeTrans,
            searchDate = saveBookingRequest.searchDate,
            secondAamount = saveBookingRequest.secondAamount,
            secondAmount = saveBookingRequest.secondAmount,
            secondReduction = saveBookingRequest.secondReduction,
            sharedExtrasIds = saveBookingRequest.sharedExtrasIds,
            start = saveBookingRequest.start,
            to = saveBookingRequest.to,
            totalFeed = saveBookingRequest.totalFeed,
            users = saveBookingRequest.users,
            usersIds = saveBookingRequest.usersIds,
            withSecondPrice = saveBookingRequest.withSecondPrice,

            )
    }


    // Reverse mapping: SaveBookingRequestDTO to SaveBookingRequest
    fun SaveBookingRequestDTOToSaveBookingRequest(saveBookingRequestDTO: SaveBookingRequestDTO): SaveBookingRequest {
        return SaveBookingRequest(
            aamount = saveBookingRequestDTO.aamount,
            amount = saveBookingRequestDTO.amount,
            amountfeeTrans = saveBookingRequestDTO.amountfeeTrans,
            bookingAnnulationDTOSet = saveBookingRequestDTO.bookingAnnulationDTOSet,
            isClient = saveBookingRequestDTO.isClient,
            closeTime = saveBookingRequestDTO.closeTime,
            couponCode = saveBookingRequestDTO.couponCode,
            currencyId = saveBookingRequestDTO.currencyId,
            currencySymbol = saveBookingRequestDTO.currencySymbol,
            decimalNumber = saveBookingRequestDTO.decimalNumber,
            description = saveBookingRequestDTO.description,
            end = saveBookingRequestDTO.end,
            Establishment = saveBookingRequestDTO.Establishment,
            establishmentPacksDTO = saveBookingRequestDTO.establishmentPacksDTO,
            establishmentPacksId = saveBookingRequestDTO.establishmentPacksId,
            establishmentPictureDTO = saveBookingRequestDTO.establishmentPictureDTO,
            facadeUrl = saveBookingRequestDTO.facadeUrl,
            from = saveBookingRequestDTO.from,
            HappyHours = saveBookingRequestDTO.HappyHours,
            mgAmount = saveBookingRequestDTO.mgAmount,
            moyFeed = saveBookingRequestDTO.moyFeed,
            numberOfPart = saveBookingRequestDTO.numberOfPart,
            numberOfPlayer = saveBookingRequestDTO.numberOfPlayer,
            openTime = saveBookingRequestDTO.openTime,
            payFromAvoir = saveBookingRequestDTO.payFromAvoir,
            plannings = saveBookingRequestDTO.plannings,
            privateExtrasIds = saveBookingRequestDTO.privateExtrasIds,
            ramountfeeTrans = saveBookingRequestDTO.ramountfeeTrans,
            reduction = saveBookingRequestDTO.reduction,
            reductionAmount = saveBookingRequestDTO.reductionAmount,
            reductionSecondAmount = saveBookingRequestDTO.reductionSecondAmount,
            reductionaAmount = saveBookingRequestDTO.reductionaAmount,
            reductionaSecondAmount = saveBookingRequestDTO.reductionaSecondAmount,
            rsamountfeeTrans = saveBookingRequestDTO.rsamountfeeTrans,
            samountfeeTrans = saveBookingRequestDTO.samountfeeTrans,
            searchDate = saveBookingRequestDTO.searchDate,
            secondAamount = saveBookingRequestDTO.secondAamount,
            secondAmount = saveBookingRequestDTO.secondAmount,
            secondReduction = saveBookingRequestDTO.secondReduction,
            sharedExtrasIds = saveBookingRequestDTO.sharedExtrasIds,
            start = saveBookingRequestDTO.start,
            to = saveBookingRequestDTO.to,
            totalFeed = saveBookingRequestDTO.totalFeed,
            users = saveBookingRequestDTO.users,
            usersIds = saveBookingRequestDTO.usersIds,
            withSecondPrice = saveBookingRequestDTO.withSecondPrice,
        )
    }
    fun SaveBookingResponseToSaveBookingResponseDTO(saveBookingResponse: SaveBookingResponse): SaveBookingResponseDTO {
    return SaveBookingResponseDTO(
        id = saveBookingResponse.id,
        from = saveBookingResponse.from ?: "",
        to = saveBookingResponse.to ?: "",
        annulationDate = saveBookingResponse.annulationDate ?: "",
        sellAmount = saveBookingResponse.sellAmount ?: BigDecimal.ZERO,
        purchaseAmount = saveBookingResponse.purchaseAmount ?: BigDecimal.ZERO,
        numberOfPlayer = saveBookingResponse.numberOfPlayer ?: 0,
        reference = saveBookingResponse.reference?: "",
        description = saveBookingResponse.description ?: "",
        isRefundable = saveBookingResponse.isRefundable ?: false,
        created = Instant.now(),
        updated = Instant.now(),
        createdBy = saveBookingResponse.createdBy ?: 0L,
        updatedBy = saveBookingResponse.updatedBy ?: 0L,
        currencyFromId = saveBookingResponse.currencyFromId ?: 0L,
        currencyToId = saveBookingResponse.currencyToId ?: 0L,
        bookingStatusId = saveBookingResponse.bookingStatusId ?: 0L,
        establishmentId = saveBookingResponse.establishmentId,
        userId = saveBookingResponse.usersIds.firstOrNull() ?: 0L,
        userLogin = saveBookingResponse.userLogin,
        establishmentName = saveBookingResponse.establishmentName,
        bookingStatusName = saveBookingResponse.bookingStatusName ?: "",
        userPhone = saveBookingResponse.userPhone,
        cancelBook = saveBookingResponse.cancelBook ?: false,
        cancel = saveBookingResponse.cancel ?: false,
        isonline = saveBookingResponse.isonline ?: false,
        activityName = saveBookingResponse.activityName ?: "",
        cityName = saveBookingResponse.cityName ?: "",
        establishmentCode = saveBookingResponse.establishmentCode ?: "",
        localAmount = saveBookingResponse.localAmount ?: BigDecimal.ZERO,
        reduction = saveBookingResponse.reduction ?: 0,
        showcancel = saveBookingResponse.showcancel ?: false,
        showfeedBack = saveBookingResponse.showfeedBack ?: false,
        bookingDate = saveBookingResponse.bookingDate ?: "",
        token = saveBookingResponse.token ?: "",
        paymentError = saveBookingResponse.paymentError ?: false,
        paymentprog = saveBookingResponse.paymentprog ?: false,
        amountToPay = saveBookingResponse.amountToPay ?: BigDecimal.ZERO,
        sobflousCode = saveBookingResponse.sobflousCode ?: "",
        couponId = saveBookingResponse.couponId ?: 0L,
        isCoupon = saveBookingResponse.isCoupon ?: false,
        couponValue = saveBookingResponse.couponValue ?: "",
        couponCode = saveBookingResponse.couponCode ?: "",
        establishmentPacksId = saveBookingResponse.establishmentPacksId ?: 0L,
        establishmentTypeCode = saveBookingResponse.establishmentTypeCode ?: "",
        isConfirmed = saveBookingResponse.isConfirmed ?: false,
        isFromEvent = saveBookingResponse.isFromEvent ?: false,
        establishmentPacksFirstTitle = saveBookingResponse.establishmentPacksFirstTitle ?: "",
        establishmentPacksSecondTitle = saveBookingResponse.establishmentPacksSecondTitle ?: "",
        usersIds = saveBookingResponse.usersIds ?: emptyList(),
        bookingUsersPaymentListDTO = saveBookingResponse.bookingUsersPaymentListDTO ?: emptyList(),
        fromStr = saveBookingResponse.fromStr ?: "",
        toStr = saveBookingResponse.toStr ?: "",
        fromStrTime = saveBookingResponse.fromStrTime ?: "",
        toStrTime = saveBookingResponse.toStrTime ?: "",
        activePayment = saveBookingResponse.activePayment ?: false,
        isWaitForPay = saveBookingResponse.isWaitForPay ?: false,
        bookingLabelId = saveBookingResponse.bookingLabelId ?: 0L,
        bookingLabelName = saveBookingResponse.bookingLabelName ?: "",
        bookingLabelColors = saveBookingResponse.bookingLabelColors ?: "",
        sharedExtrasIds = saveBookingResponse.sharedExtrasIds ?: emptyList(),
        privateExtrasIds = saveBookingResponse.privateExtrasIds ?: emptyList(),
        privateExtrasLocalIds = saveBookingResponse.privateExtrasLocalIds ?: mutableMapOf(),
        userFirstName = saveBookingResponse.userFirstName ?: "",
        userLastName = saveBookingResponse.userLastName ?: "",
        extras = saveBookingResponse.extras ?: emptyList(),
        numberOfPart = saveBookingResponse.numberOfPart ?: 0,
        createdStr = saveBookingResponse.createdStr ?: ""
    )
}



}






