package com.nevaDev.padeliummarhaba.models

data class TimeSlotResponse(
    private val plannings: List<PlanningDTO> = mutableListOf(),

    )

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