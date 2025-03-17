package com.padelium.data.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.happyHours
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.bookingAnnulationDTOSet
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
data class GetBookingResponseDTO(
    @JsonAdapter(EstablishmentDTOAdapter::class)
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

class EstablishmentDTOAdapter : JsonDeserializer<EstablishmentDTO?> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): EstablishmentDTO? {
        return if (json.isJsonObject) {
            context.deserialize(json, EstablishmentDTO::class.java)
        } else {
            null // Handle or log cases where it's a string
        }
    }
}
// @JsonAdapter(EstablishmentDTOAdapter::class)