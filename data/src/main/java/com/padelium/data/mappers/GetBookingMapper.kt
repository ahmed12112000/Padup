package com.padelium.data.mappers
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.EstablishmentDTO
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject

class GetBookingMapper @Inject constructor() {



    fun GetBookingResponseToGetBookingResponseDto(getBookingResponse: List<GetBookingResponse>): List<GetBookingResponseDTO> {


        return getBookingResponse.map { response ->


            // Function to safely parse the date and return the formatted result or fallback


            GetBookingResponseDTO(
                establishmentDTO = response.establishmentDTO,
                description = response.description ?: "",
                amount = response.amount ?: BigDecimal.ZERO,
                decimalNumber = response.decimalNumber ?: 0,
                currencySymbol = response.currencySymbol ?: "",
                facadeUrl = response.facadeUrl ?: "",
                openTime = response.openTime?.toString() ?: Instant.now().toString(),
                closeTime = response.closeTime?.toString() ?: Instant.now().toString(),

                searchDate = response.searchDate ?: "",
                from = response.from,
                to = response.to,
                numberOfPlayer = response.numberOfPlayer ?: 0,
                currencyId = response.currencyId ?: 0L,
                mgAmount = response.mgAmount ?: BigDecimal.ZERO,
                totalFeed = response.totalFeed ?: 0,
                moyFeed = response.moyFeed ?: 0.0,
                bookingAnnulationDTOSet = response.bookingAnnulationDTOSet ?: emptyList(),
                secondAmount = response.secondAmount ?: BigDecimal.ZERO,
                secondAamount = response.secondAamount ?: BigDecimal.ZERO,
                HappyHours = response.HappyHours ?: emptyList(),
                withSecondPrice = response.withSecondPrice ?: false,
                reductionAmount = response.reductionAmount ?: BigDecimal.ZERO,
                reductionSecondAmount = response.reductionSecondAmount ?: BigDecimal.ZERO,
                payFromAvoir = response.payFromAvoir ?: false,
                reduction = response.reduction ?: 0,
                reductionaAmount = response.reductionaAmount ?: BigDecimal.ZERO,
                reductionaSecondAmount = response.reductionaSecondAmount ?: BigDecimal.ZERO,
                start = response.start?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
                end = response.end,
                amountfeeTrans = response.amountfeeTrans ?: BigDecimal.ZERO,
                samountfeeTrans = response.samountfeeTrans ?: BigDecimal.ZERO,
                ramountfeeTrans = response.ramountfeeTrans ?: BigDecimal.ZERO,
                rsamountfeeTrans = response.rsamountfeeTrans ?: BigDecimal.ZERO,
                couponCode = response.couponCode ?: "",
                establishmentPacksDTO = response.establishmentPacksDTO ?: emptyList(),

                establishmentPacksId = if (response.establishmentPacksId == 0L) null else response.establishmentPacksId,

                orderId = response.orderId ?: 0L,
                plannings = response.plannings ?: emptyList(),
                users = response.users ?: emptyList(),
                client = true ,
                secondReduction = response.secondReduction ?: 0,
                aamount = response.aamount ?: BigDecimal.ZERO,
                EstablishmentPictureDTO = response.EstablishmentPictureDTO ?: emptyList(),
                numberOfPart =  response.numberOfPart ?: 0,
                sharedExtrasIds = response.sharedExtrasIds ?: emptyList(),
                userIds = response.userIds ?: emptyList(),
                id = response.id ?: 0L,
                privateExtrasIds = response.privateExtrasIds ?: emptyList(),
                buyerId = response.buyerId ?: "",
                couponIds = response.couponIds ?: emptyMap(),

                )
        }
    }
}

fun deserializeEstablishmentDTO(jsonElement: JsonElement): List<EstablishmentDTO> {
    val gson = Gson()
    return if (jsonElement.isJsonArray) {
        gson.fromJson(jsonElement, object : TypeToken<List<EstablishmentDTO>>() {}.type)
    } else if (jsonElement.isJsonObject) {
        listOf(gson.fromJson(jsonElement, EstablishmentDTO::class.java))
    } else {
        emptyList()
    }
}


private fun parseInstant(dateString: String?): Instant {
    return try {
        dateString?.let { Instant.parse(it) } ?: Instant.now()
    } catch (e: Exception) {
        Log.e("DateParseError", "Failed to parse date: $dateString", e)
        Instant.now()
    }
}