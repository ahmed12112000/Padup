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


    fun GetBookingResponseToGetBookingResponseDto(getBookingResponse: List<GetBookingResponse>): List<GetBookingResponseDTO> {
        val gson = Gson()

        return getBookingResponse.map { response ->

            // Parse the EstablishmentDTO from the response
            val establishmentDTOList: List<EstablishmentDTO> = try {
                val establishmentJson = response.EstablishmentDTO

                // Check if the establishmentJson is a valid JsonElement
                if (establishmentJson is JsonElement) {
                    when {
                        // If it's a JSON array, parse it into a list of EstablishmentDTO objects
                        establishmentJson.isJsonArray -> gson.fromJson(establishmentJson, Array<EstablishmentDTO>::class.java).toList()

                        // If it's a JSON object, wrap it into a list
                        establishmentJson.isJsonObject -> listOf(gson.fromJson(establishmentJson, EstablishmentDTO::class.java))

                        // If it's neither an array nor an object, return an empty list
                        else -> emptyList()
                    }
                } else {
                    emptyList()  // Return an empty list if it's not a JsonElement
                }
            } catch (e: Exception) {
                Log.e("ParsingError", "Failed to parse EstablishmentDTO: ${e.message}")
                emptyList()  // Return an empty list if parsing fails
            }

            // If establishmentDTOList is empty, ensure name is a default value
            val establishmentDTOWithNames = establishmentDTOList.map {
                Log.d("EstablishmentName", "Establishment name: ${it.name ?: "Unknown"}")

                it.copy(name = it.name ?: "Unknown")
            }

            // Return the final DTO with the list of EstablishmentDTO
            GetBookingResponseDTO(
                EstablishmentDTO = response.EstablishmentDTO?: emptyList(),
                amount = response.amount ?: 0.0,
                decimalNumber = response.decimalNumber ?: 0,
                currencySymbol = response.currencySymbol ?: "",
                facadeUrl = response.facadeUrl ?: "",
                openTime = response.openTime ?: Instant.now(),
                closeTime = response.closeTime ?: Instant.now(),
                searchDate = response.searchDate ?: "",
                from = response.from ?: "",
                to = response.to ?: "",
                numberOfPlayer = response.numberOfPlayer ?: 0,
                description = response.description ?: "",
                currencyId = response.currencyId ?: 0,
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
                isClient = response.isClient ?: true,
                secondReduction = response.secondReduction ?: 0,
                aamount = response.aamount ?: BigDecimal.ZERO,
                EstablishmentPictureDTO = response.EstablishmentPictureDTO ?: emptyList(),
                key = response.key?: ""

                )
        }
    }
    fun GetBookingDataResultToGetBookingDataResultDto(getBookingDataResult: List<GetBookingDataResult>): List<GetBookingDataResultDto> {
        return getBookingDataResult.map { response ->
            GetBookingDataResultDto(
                name = response.name ?: "",
                amount = response.amount ?: 0.0,
                currencySymbol = response.currencySymbol ?: "",
                fromStr = response.fromStr ?: "",
                toStr = response.toStr ?: "",


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