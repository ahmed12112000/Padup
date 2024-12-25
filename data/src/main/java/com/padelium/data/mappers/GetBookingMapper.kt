package com.padelium.data.mappers
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.padelium.data.dto.GetBookingDataResultDto
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.GetBookingDataResult
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.EstablishmentDTO
import java.math.BigDecimal
import java.time.Instant
import javax.inject.Inject

class GetBookingMapper @Inject constructor() {

    fun GetBookingResponseDtoToGetBookingResponse(dtos: List<GetBookingResponseDTO>): List<GetBookingResponse> {
        return dtos.map { dto ->
            GetBookingResponse(
                // Map fields from GetBookingResponseDTO to GetBookingResponse
                aamount = dto.aamount ?: BigDecimal.ZERO,
                amount = dto.amount ?: 0.0,
                amountfeeTrans = dto.amountfeeTrans ?: BigDecimal.ZERO,
                bookingAnnulationDTOSet = dto.bookingAnnulationDTOSet ?: emptyList(),
                isClient = dto.isClient ?: true,
                closeTime = dto.closeTime?.toString() ?: Instant.now().toString(),

                couponCode = dto.couponCode ?: "",
                currencyId = dto.currencyId ?: 0L,
                currencySymbol = dto.currencySymbol ?: "",
                decimalNumber = dto.decimalNumber ?: 0,
                description = dto.description ?: "",
                end = dto.end ?: "",
                establishmentDTO = dto.establishmentDTO,
                establishmentPacksDTO = dto.establishmentPacksDTO ?: emptyList(),
                establishmentPacksId = dto.establishmentPacksId ?: 0L,
                EstablishmentPictureDTO = dto.EstablishmentPictureDTO ?: emptyList(),
                facadeUrl = dto.facadeUrl ?: "",
                from = dto.from ?: "",
                HappyHours = dto.HappyHours ?: emptyList(),
                mgAmount = dto.mgAmount ?: BigDecimal.ZERO,
                moyFeed = dto.moyFeed ?: 0.0,
                numberOfPart = dto.numberOfPart ?: 0.0,
                numberOfPlayer = dto.numberOfPlayer ?: 0,
                openTime = dto.openTime?.toString() ?: Instant.now().toString(),
                payFromAvoir = dto.payFromAvoir ?: false,
                plannings = dto.plannings ?: emptyList(),
                privateExtrasIds = dto.privateExtrasIds ?: emptyList(),
                ramountfeeTrans = dto.ramountfeeTrans ?: BigDecimal.ZERO,
                reduction = dto.reduction ?: BigDecimal.ZERO,
                reductionAmount = dto.reductionAmount ?: BigDecimal.ZERO,
                reductionSecondAmount = dto.reductionSecondAmount ?: BigDecimal.ZERO,
                reductionaAmount = dto.reductionaAmount ?: BigDecimal.ZERO,
                reductionaSecondAmount = dto.reductionaSecondAmount ?: BigDecimal.ZERO,
                rsamountfeeTrans = dto.rsamountfeeTrans ?: BigDecimal.ZERO,
                samountfeeTrans = dto.samountfeeTrans ?: BigDecimal.ZERO,
                searchDate = dto.searchDate ?: "",
                secondAamount = dto.secondAamount ?: BigDecimal.ZERO,
                secondAmount = dto.secondAmount ?: BigDecimal.ZERO,
                secondReduction = dto.secondReduction ?: 0,
                sharedExtrasIds = dto.sharedExtrasIds ?: emptyList(),
                start = dto.start?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
                to = dto.to?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),

                totalFeed = dto.totalFeed ?: 0,
                users = dto.users ?: emptyList(),
                usersIds = dto.usersIds ?: emptyList(),
                withSecondPrice = dto.withSecondPrice ?: false,
                )
        }
    }






    fun GetBookingResponseToGetBookingResponseDto(getBookingResponse: List<GetBookingResponse>): List<GetBookingResponseDTO> {
        return getBookingResponse.map { response ->

            GetBookingResponseDTO(
                establishmentDTO = response.establishmentDTO,
                description = response.description ?: "",
                amount = response.amount ?: 0.0,
                decimalNumber = response.decimalNumber ?: 0,
                currencySymbol = response.currencySymbol ?: "",
                facadeUrl = response.facadeUrl ?: "",
                openTime = response.openTime?.toString() ?: Instant.now().toString(),
                closeTime = response.closeTime?.toString() ?: Instant.now().toString(),

                searchDate = response.searchDate ?: "",
                from = response.from?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),

                to = response.to?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),

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
                reduction = response.reduction ?: BigDecimal.ZERO,
                reductionaAmount = response.reductionaAmount ?: BigDecimal.ZERO,
                reductionaSecondAmount = response.reductionaSecondAmount ?: BigDecimal.ZERO,
                start = response.start?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),

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
                isClient = response.isClient ?: true,
                secondReduction = response.secondReduction ?: 0,
                aamount = response.aamount ?: BigDecimal.ZERO,
                EstablishmentPictureDTO = response.EstablishmentPictureDTO ?: emptyList(),
                numberOfPart = response.numberOfPart ?: 0.0,
                privateExtrasIds = response.privateExtrasIds ?: emptyList(),
                sharedExtrasIds = response.sharedExtrasIds ?: emptyList(),
                usersIds = response.usersIds ?: emptyList(),

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