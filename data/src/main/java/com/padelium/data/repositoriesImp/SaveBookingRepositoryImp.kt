package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.mappers.GetBookingMapper
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.repositories.ISaveBookingRepository
import retrofit2.Response
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.math.BigDecimal
import java.net.Proxy
import java.time.Instant
import com.google.gson.*
import java.lang.reflect.Type

class SaveBookingRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : ISaveBookingRepository {

    override suspend fun SaveBooking(saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>> {
        // Directly pass the SaveBookingRequest to the API
        return api.SaveBooking(saveBookingRequest)
    }
}

//


// Custom serializer
class SaveBookingRequestSerializer : JsonSerializer<SaveBookingRequest> {
    override fun serialize(
        src: SaveBookingRequest?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        // Serialize only the bookings list
        return JsonArray().apply {
            src?.bookings?.forEach { booking ->
                // Serialize each booking and add it to the JsonArray
                add(Gson().toJsonTree(booking))
            }
        }
    }
}

