package com.padelium.domain.dto

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import java.math.BigDecimal
import java.time.Instant
import java.lang.reflect.Type

//    java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 23 path $[0].establishmentDTO

//    java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1 column 242 path $[0].establishmentDTO.created

//    Parameter specified as non null is
data class GetBookingResponse(
    @JsonAdapter(EstablishmentDTOAdapter::class)
    val privateExtrasIds:List<Long?>,
    val reduction: BigDecimal?,
    val sharedExtrasIds: List<Long?>,
    val usersIds: List<Long?>,

    val numberOfPart: Double?,
    val establishmentDTO: EstablishmentDTO,

    val description: String?,


    // val establishmentFeatureDTOList: EstablishmentDTO,






    val amount: Double?,
    val decimalNumber: Int?,
    val currencySymbol: String?,
    val facadeUrl: String?,

    val openTime: String?,
    val closeTime: String?,

    val searchDate: String?,
    val from: String?,
    val to: String?,
    val numberOfPlayer: Int?,
    val currencyId: Long?,
    val mgAmount: BigDecimal?,
    val totalFeed: Int?,
    val moyFeed: Double?,
    val bookingAnnulationDTOSet: List<Unit>,
    val secondAmount: BigDecimal?,
    val secondAamount: BigDecimal?,

    val HappyHours: List<HappyHours>,

    val withSecondPrice: Boolean?,
    val reductionAmount: BigDecimal?,
    val reductionSecondAmount: BigDecimal?,
    val payFromAvoir: Boolean?,
    val reductionaAmount: BigDecimal?,
    val reductionaSecondAmount: BigDecimal?,
    val start: String?,
    val end: String?,
    val amountfeeTrans: BigDecimal?,
    val samountfeeTrans: BigDecimal?,
    val ramountfeeTrans: BigDecimal?,
    val rsamountfeeTrans: BigDecimal?,
    val couponCode: String?,
    val establishmentPacksDTO: List<EstablishmentPacksDTO>,
    val establishmentPacksId: Long?,
    val plannings: List<PlanningDTO>,
    val users: List<Long?>,
    val isClient: Boolean = true,
    val secondReduction: Int?,
    val aamount: BigDecimal?,
    val EstablishmentPictureDTO: List<EstablishmentPictureDTO>,

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