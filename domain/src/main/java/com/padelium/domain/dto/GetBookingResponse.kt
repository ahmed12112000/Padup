package com.padelium.domain.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.math.BigDecimal
import java.lang.reflect.Type

class LongNullSerializer : JsonSerializer<Long?> {
    override fun serialize(src: Long?, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return if (src == null) {
            JsonNull.INSTANCE
        } else {
            JsonPrimitive(src)
        }
    }
}


// Gson configuration with the custom serializer
val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Long::class.java, LongNullSerializer()) // Register custom serializer for Long?
    .serializeNulls() // Ensure null values are serialized as "null"
    .create()

data class GetBookingResponse(
    val aamount: BigDecimal?,
    val amount: BigDecimal?,
    val amountfeeTrans: BigDecimal?,
    val bookingAnnulationDTOSet: Set<bookingAnnulationDTOSet?> = emptySet(),
    val end: String?,
    val establishmentDTO: EstablishmentDTO?,
    val from: String?,
    val numberOfPart: Int?,
    val payFromAvoir: Boolean?,
    val searchDate: String?,
    val start: String?,
    val to: String?,
    val userIds: List<Long?>,
    val plannings: List<PlanningDTO>,
    //   val users: String,
    val privateExtrasIds: List<Long?>,
    val sharedExtrasIds: List<Long?>,
    val establishmentPacksDTO: List<EstablishmentPacksDTO?>,
    val currencyId: Long?,

    )
