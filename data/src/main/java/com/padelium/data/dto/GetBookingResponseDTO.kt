package com.padelium.data.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.padelium.domain.dto.EstablishmentPacksDTO
import com.padelium.domain.dto.EstablishmentPictureDTO
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.HappyHours
import com.padelium.domain.dto.contactDTO
import java.math.BigDecimal
import java.time.Instant
import java.lang.reflect.Type

data class GetBookingResponseDTO(
    @JsonAdapter(EstablishmentDTOAdapter::class)
    @SerializedName("privateExtrasIds") val privateExtrasIds: List<Long?>,
    @SerializedName("reduction") val reduction: BigDecimal?,
    @SerializedName("sharedExtrasIds") val sharedExtrasIds: List<Long?>,
    @SerializedName("usersIds") val usersIds: List<Long?>,
    @SerializedName("orderId") val orderId: Long?,
    @SerializedName("id") val id: Long?,

    @SerializedName("numberOfPart") val numberOfPart: Double?,
    @SerializedName("establishmentDTO") val establishmentDTO: EstablishmentDTO,

    @SerializedName("description") val description: String?,

    // @SerializedName("establishmentFeatureDTOList") val establishmentFeatureDTOList: EstablishmentDTO,

    @SerializedName("amount") val amount: Double?,
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

    @SerializedName("HappyHours") val HappyHours: List<HappyHours>,

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
    @SerializedName("users") val users: List<Long?>,
    @SerializedName("isClient") val isClient: Boolean = true,
    @SerializedName("secondReduction") val secondReduction: Int?,
    @SerializedName("aamount") val aamount: BigDecimal?,
    @SerializedName("EstablishmentPictureDTO") val EstablishmentPictureDTO: List<EstablishmentPictureDTO>
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