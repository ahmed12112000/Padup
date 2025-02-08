package com.nevaDev.padeliummarhaba.repository.Reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservationViewModel : ViewModel() {

    private val _reservationKey = MutableStateFlow<String?>(null)
    val reservationKey: StateFlow<String?> = _reservationKey

    private val _availableEstablishments = MutableStateFlow<List<BookingResponse>>(emptyList())
    val availableEstablishments: StateFlow<List<BookingResponse>> = _availableEstablishments

    fun createReservation(dateTime: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.createReservationKey(
                    ReservationRequest(
                        dateTime = dateTime,
                        activityId = null,
                        cityId = null,
                        activityName = null,
                        cityName = null,
                        establishmentId = null,
                        iscity = false,
                        time = null
                    )
                )
                _reservationKey.value = response.key
                fetchAvailableEstablishments(response)
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }

    private fun fetchAvailableEstablishments(reservationResponse: ReservationResponse) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.fetchAvailableEstablishments(reservationResponse)
                _availableEstablishments.value = response
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }
}
