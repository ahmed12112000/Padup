package com.nevaDev.padeliummarhaba.models

import org.threeten.bp.Instant
import java.io.Serializable
import java.math.BigDecimal

data class  CombinedEstablishment(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val email: String,
    val adress: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val created: Instant,
    val updated: Instant,
    val createdBy: Long,
    val updatedBy: Long,
    val cityId: Long,
    val activityId: Long,
    val jhiEntityId: Long,
    val validated: Boolean,
    val maxNumberPlayer: Int,
    val timeSpan: BigDecimal,
    val cityName: String,
    val activityName: String,
    val userId: Long,
    val userEmail: String,
    val activityCode: String,
    val detailDescription: String,
    val activityValide: Boolean,
    val phone: String,
    val establishmentFeatureDTOList: List<establishmentFeatureDTO>,
    val contacts: List<contactDTO>,
    val createdByName: String,
    val createdByEmail: String,
    val logo: String,
    val pictures: List<Unit>,
    val facadePict: Boolean,
    val facadePict1: Boolean,
    val facadePict2: Boolean,
    val facadePict3: Boolean,
    val color: String,
    val establishmentTypeId: Long,
    val establishmentTypeCode: String,
    val isEvent: Boolean = false,
    val isSpace: Boolean = false,
    val showAsGold: Boolean = false,
    val activityActive: Boolean,
    val activitySmallIcon: String,
    val activityIcon: String,
    val isClient: Boolean = true,
    val establishmentId: Long,
    val createdDate: Instant,
    val amount: Double,
    val from: Instant,
    val to: Instant,
    val available: Int,
    val openTime: Instant,
    val closeTime: Instant,
    val bookings: List<bookingDTO>,
    val availableBol: Boolean,
    val dayWithBooking: Boolean,
    val fromStr: String,
    val toStr: String,
    val price: BigDecimal,
    val feeTransaction: BigDecimal,
    val reductionPrice: BigDecimal,
    val rfeeTransaction: BigDecimal,
    val currencySymbol: String,
    val reductionPriceBol: Boolean,
    val secondPrice: Boolean,
    val isHappyHours: Boolean,
    val annulationDate: String,
    ): Serializable

/*
data class PlanningDTO(
    val from: Instant,
    val to: Instant,
    val available: Int,
    val openTime: Instant,
    val closeTime: Instant,
    val bookings: List<bookingDTO>,
    val availableBol: Boolean,
    val dayWithBooking: Boolean,
    val fromStr: String,
    val toStr: String,
    val price: BigDecimal,
    val feeTransaction: BigDecimal,
    val reductionPrice: BigDecimal,
    val rfeeTransaction: BigDecimal,
    val currencySymbol: String,
    val reductionPriceBol: Boolean,
    val secondPrice: Boolean,
    val isHappyHours: Boolean,
    val annulationDate: String,
) : Serializable



 */
/*
 fun GetBookingList.toPlanningDTO(): PlanningDTO {
        return PlanningDTO(

            from = this.from,
            annulationDate = this.annulationDate,
            isHappyHours = this.isHappyHours,
            secondPrice = this.secondPrice,
            reductionPriceBol = this.reductionPriceBol,
            reductionaSecondAmount = this.reductionaSecondAmount,
            currencySymbol = this.currencySymbol,
            rfeeTransaction = this.rfeeTransaction,
            reductionPrice = this.reductionPrice,
            feeTransaction = this.feeTransaction,
            price = this.price,
            toStr = this.toStr,
            fromStr = this.fromStr,
            dayWithBooking = this.dayWithBooking,
            availableBol = this.availableBol,
            bookings = this.bookings,
            closeTime = this.closeTime,
            openTime = this.openTime,
            available = this.available,
            to = this.to
        )
    }


 */
/*
 fun GetBookingList.toPlanningDTO(): PlanningDTO {
        return PlanningDTO(
            Establishment = this.Establishment, // Maps establishments directly
            establishmentPictureDTO = this.establishmentPictureDTO, // Maps picture DTOs
            amount = this.amount, // Maps amount
            decimalNumber = this.decimalNumber, // Maps decimal number
            currencySymbol = this.currencySymbol, // Maps currency symbol
            facadeUrl = this.facadeUrl, // Maps facade URL
            openTime = this.openTime, // Maps open time
            closeTime = this.closeTime, // Maps close time
            searchDate = this.searchDate, // Maps search date
            from = this.from, // Maps from timestamp
            to = this.to, // Maps to timestamp
            numberOfPlayer = this.numberOfPlayer, // Maps number of players
            description = this.description, // Maps description
            currencyId = this.currencyId, // Maps currency ID
            mgAmount = this.mgAmount, // Maps mg amount
            totalFeed = this.totalFeed, // Maps total feed
            moyFeed = this.moyFeed, // Maps moy feed
            bookingAnnulationDTOSet = this.bookingAnnulationDTOSet, // Maps booking annulation set
            secondAmount = this.secondAmount, // Maps second amount
            secondAamount = this.secondAamount, // Maps second alternative amount
            HappyHours = this.HappyHours, // Maps HappyHours list
            withSecondPrice = this.withSecondPrice, // Maps second price flag
            reductionAmount = this.reductionAmount, // Maps reduction amount
            reductionSecondAmount = this.reductionSecondAmount, // Maps second reduction amount
            payFromAvoir = this.payFromAvoir, // Maps payment from avoir
            reduction = this.reduction, // Maps reduction percentage/int
            reductionaAmount = this.reductionaAmount, // Maps primary reduction amount
            reductionaSecondAmount = this.reductionaSecondAmount, // Maps secondary reduction amount
            start = this.start, // Maps start string
            end = this.end, // Maps end string
            amountfeeTrans = this.amountfeeTrans, // Maps amount fee transaction
            samountfeeTrans = this.samountfeeTrans, // Maps secondary amount fee transaction
            ramountfeeTrans = this.ramountfeeTrans, // Maps refunded amount fee transaction
            rsamountfeeTrans = this.rsamountfeeTrans, // Maps refunded secondary amount fee transaction
            couponCode = this.couponCode, // Maps coupon code
            establishmentPacksDTO = this.establishmentPacksDTO, // Maps establishment packs
            establishmentPacksId = this.establishmentPacksId, // Maps establishment pack ID
            plannings = this.plannings, // Maps plannings list
            users = this.users, // Maps users list
            isClient = this.isClient, // Maps client status
            secondReduction = this.secondReduction, // Maps second reduction
            aamount = this.aamount // Maps alternative amount
        )
    }


 */
