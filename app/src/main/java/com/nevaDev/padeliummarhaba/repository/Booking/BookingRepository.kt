package com.nevaDev.padeliummarhaba.repository.Booking

class BookingRepository(private val api: BookingApi) {

    suspend fun getAvailableTimeSlots(token: String, date: String): List<TimeSlot>? {
        val response = api.getAvailableTimeSlots("Bearer $token", date)
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}
