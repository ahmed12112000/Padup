package com.padelium.domain.dto

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.lang.reflect.Type
import java.time.LocalDateTime

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
    @SerializedName("privateExtrasIds") val privateExtrasIds: List<Long?>,
    @SerializedName("reduction") val reduction: Int?,
    @SerializedName("sharedExtrasIds") val sharedExtrasIds: List<Long?>,
 //   @SerializedName("orderId") val orderId: Long?,
 //   @SerializedName("id") val id: Long?,
    @SerializedName("numberOfPart") val numberOfPart: Int,
    @SerializedName("establishmentDTO") val establishmentDTO: EstablishmentDTO,
    @SerializedName("description") val description: String?,
 //   @SerializedName("buyerId") val buyerId: String?,
   // @SerializedName("couponIds") val couponIds: Map<Long, Long>?,
    @SerializedName("amount") val amount: BigDecimal?,
    @SerializedName("decimalNumber") val decimalNumber: Int?,
    @SerializedName("currencySymbol") val currencySymbol: String?,
    @SerializedName("facadeUrl") val facadeUrl: String?,
    @SerializedName("openTime") val openTime: String?,
    @SerializedName("closeTime") val closeTime: String?,
    @SerializedName("searchDate") val searchDate: String?,
    @SerializedName("from") val from: String?,
    @SerializedName("to") val to: String?,
    @SerializedName("numberOfPlayer") val numberOfPlayer: Int?,
    @SerializedName("currencyId") val currencyId: Long?,
    @SerializedName("mgAmount") val mgAmount: BigDecimal?,
    @SerializedName("totalFeed") val totalFeed: Int?,
    @SerializedName("moyFeed") val moyFeed: Double?,
    @SerializedName("bookingAnnulationDTOSet") val bookingAnnulationDTOSet: List<Unit>,
    @SerializedName("secondAmount") val secondAmount: BigDecimal?,
    @SerializedName("secondAamount") val secondAamount: BigDecimal?,
    @SerializedName("HappyHours") val happyHours: String?,
    @SerializedName("withSecondPrice") val withSecondPrice: Boolean?,
    @SerializedName("reductionAmount") val reductionAmount: BigDecimal?,
    @SerializedName("reductionSecondAmount") val reductionSecondAmount: BigDecimal?,
    @SerializedName("payFromAvoir") val payFromAvoir: Boolean?,
    @SerializedName("reductionaAmount") val reductionaAmount: BigDecimal?,
    @SerializedName("reductionaSecondAmount") val reductionaSecondAmount: BigDecimal?,
    @SerializedName("start") val start: String?,
    @SerializedName("end") val end: String?,
    @SerializedName("amountfeeTrans") val amountfeeTrans: BigDecimal?,
    @SerializedName("samountfeeTrans") val samountfeeTrans: BigDecimal?,
    @SerializedName("ramountfeeTrans") val ramountfeeTrans: BigDecimal?,
    @SerializedName("rsamountfeeTrans") val rsamountfeeTrans: BigDecimal?,
    @SerializedName("couponCode") val couponCode: String?,
    @SerializedName("establishmentPacksDTO") val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    @SerializedName("establishmentPacksId") val establishmentPacksId: Long?,
    @SerializedName("plannings") val plannings: List<PlanningDTO>,
   // @SerializedName("users") val users: List<Long?>,

    @SerializedName("userIds") val userIds: List<Long?>,

    @SerializedName("client") val client: Boolean = true,
    @SerializedName("secondReduction") val secondReduction: Int?,
    @SerializedName("aamount") val aamount: BigDecimal?,
    @SerializedName("EstablishmentPictureDTO") val EstablishmentPictureDTO: List<EstablishmentPictureDTO>
)
