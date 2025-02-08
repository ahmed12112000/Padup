package com.nevaDev.padeliummarhaba.repository.Booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookingViewModel(private val repository: BookingRepository) : ViewModel() {

    var timeSlots: List<TimeSlot>? = null
        private set

    fun fetchTimeSlots(token: String, date: String) {
        viewModelScope.launch {
            timeSlots = repository.getAvailableTimeSlots(token, date)
        }
    }
}
