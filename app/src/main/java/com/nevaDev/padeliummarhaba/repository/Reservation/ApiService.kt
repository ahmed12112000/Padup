package com.nevaDev.padeliummarhaba.repository.Reservation

import retrofit2.http.Body
import retrofit2.http.POST

// Data class for reservation request
data class ReservationRequest(
    val dateTime: String,
    val activityId: Int?,
    val cityId: Int?,
    val activityName: String?,
    val cityName: String?,
    val establishmentId: Int?,
    val iscity: Boolean,
    val time: String?
)

// Data class for reservation response
data class ReservationResponse(
    val key: String,
    val dateTime: String
)

// Data class for booking response
data class BookingResponse(
    val establishmentDTO: EstablishmentDTO
)

// Establishment DTO
data class EstablishmentDTO(
    val id: Int,
    val name: String,
    val code: String,
    val description: String?,
    val email: String?
)

interface ApiService {
    @POST("api/establishments/search/init")
    suspend fun createReservationKey(@Body request: ReservationRequest): ReservationResponse

    @POST("api/establishments/search/init/booking")
    suspend fun fetchAvailableEstablishments(@Body request: ReservationResponse): List<BookingResponse>
}
