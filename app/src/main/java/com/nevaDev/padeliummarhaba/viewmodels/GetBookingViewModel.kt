package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.mappers.GetBookingMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.GetBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class GetBookingViewModel @Inject constructor(
    private val getBookingUseCase: GetBookingUseCase,
    private val getBookingMapper: GetBookingMapper
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<List<GetBookingResponseDTO>>>()

    private val _timeSlots = MutableLiveData<List<TimeSlot>>()

    private val _isUserLoggedIn1 = mutableStateOf(false)
    val isUserLoggedIn1: State<Boolean> get() = _isUserLoggedIn1

    private val _filteredTimeSlots = MutableLiveData<List<TimeSlot>>()
    val filteredTimeSlots: LiveData<List<TimeSlot>> get() = _filteredTimeSlots

    private val _parsedTimeSlotss = MutableStateFlow<List<TimeSlot>>(emptyList())
    val parsedTimeSlots: StateFlow<List<TimeSlot>> get() = _parsedTimeSlotss

    private val _selectedBookings = MutableLiveData<List<GetBookingResponseDTO>>(emptyList())
    private val _getBookingResponseDTO = MutableLiveData<GetBookingResponseDTO>()

    val selectedBookings: LiveData<List<GetBookingResponseDTO>> get() = _selectedBookings

    fun updateBookings(newBookings: List<GetBookingResponseDTO>) {
        _selectedBookings.value = newBookings
    }
    fun updateLoginState(isLoggedIn1: Boolean) {
        _isUserLoggedIn1.value = isLoggedIn1
    }
    fun getBooking(key: String, selectedDate: LocalDate) {
        dataResultBooking.value = DataResultBooking.Loading
        viewModelScope.launch {
            try {
                val result = getBookingUseCase.execute(key)
                dataResultBooking.value = when (result) {
                    is DataResultBooking.Success -> {
                        val mappedData = getBookingMapper.GetBookingResponseToGetBookingResponseDto(result.data)

                        // Parse and populate time slots using the provided selectedDate
                        val parsedTimeSlots = mappedData.flatMap { booking ->
                            booking.plannings.mapNotNull { planning ->
                                // Check if the planning is available
                                val timeSlot = parseTimeSlot(planning.fromStr, selectedDate)
                                // Only return time slots that are available (not reserved)
                                if (timeSlot != null && isAvailable(timeSlot)) {
                                    timeSlot // Only return available time slots
                                } else {
                                    null
                                }
                            }
                        }


                        // Populate _timeSlots
                        _timeSlots.postValue(parsedTimeSlots)

                        // Filter slots after populating _timeSlots
                        if (parsedTimeSlots.isNotEmpty()) {
                            filterSlotsByDate(selectedDate, parsedTimeSlots) // Pass selectedDate here
                            _parsedTimeSlotss.value = parsedTimeSlots
                        } else {
                            _filteredTimeSlots.postValue(emptyList())
                        }

                        DataResultBooking.Success(mappedData) // Return success with mapped data
                    }

                    is DataResultBooking.Failure -> DataResultBooking.Failure(
                        exception = result.exception,
                        errorCode = result.errorCode,
                        errorMessage = result.errorMessage
                    )

                    else -> DataResultBooking.Failure(null, null, "Unexpected error occurred.")
                }
            } catch (e: Exception) {
                dataResultBooking.value = DataResultBooking.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = e.localizedMessage ?: "An error occurred"
                )
            }
        }
    }
    private fun isAvailable(timeSlot: TimeSlot): Boolean {
        val bookedSlots = _selectedBookings.value?.flatMap { booking ->
            booking.plannings.mapNotNull { planning ->
                parseTimeSlot(planning.fromStr, timeSlot.date)
            }
        } ?: emptyList()

        // Return true if the timeSlot is not found in the list of booked slots
        return bookedSlots.none { it.date == timeSlot.date && it.time == timeSlot.time }
    }




    private fun parseTimeSlot(fromStr: String, date: LocalDate): TimeSlot? {
        return try {
            val localTime =
                LocalTime.parse(fromStr, DateTimeFormatter.ofPattern("H:mm")) // Parse the time
            TimeSlot(date = date, time = localTime) // Use LocalDate and LocalTime
        } catch (e: Exception) {
            null
        }
    }

    fun filterSlotsByDate(selectedDate: LocalDate, parsedTimeSlots: List<TimeSlot>) {
        viewModelScope.launch {
            try {
                val currentDateTime = ZonedDateTime.now(ZoneId.of("Africa/Tunis"))
                val isToday = selectedDate.isEqual(currentDateTime.toLocalDate())

                if (parsedTimeSlots.isEmpty()) {
                    _filteredTimeSlots.postValue(emptyList())
                    return@launch
                }

                val filteredSlots = parsedTimeSlots.filter { slot ->
                    val slotDateTime = ZonedDateTime.of(slot.date.atTime(slot.time), ZoneId.of("Africa/Tunis"))

                    if (isToday) {
                        // Include only slots for today that are after the current time
                        slotDateTime.isAfter(currentDateTime)
                    } else {
                        // For any other date, include all slots on that date
                        slot.date.isEqual(selectedDate)
                    }
                }

                Log.d("FilterSlotsByDate", "Filtered slots for $selectedDate: $filteredSlots")
                _filteredTimeSlots.postValue(filteredSlots)

            } catch (e: Exception) {
                Log.e("FilterSlotsByDate", "Error filtering slots: ${e.message}", e)
                _filteredTimeSlots.postValue(emptyList())
            }
        }
    }




}





// Updated TimeSlot data class with LocalDate and LocalTime types


// Updated parseTimeSlot function




data class TimeSlot(
    val date: LocalDate, // Store as LocalDate for better comparisons
    val time: LocalTime  // Store as LocalTime for accurate time comparisons
)
