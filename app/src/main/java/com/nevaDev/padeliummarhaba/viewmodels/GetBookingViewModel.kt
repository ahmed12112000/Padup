package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
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
    fun getBooking(key: String) {
        dataResultBooking.value = DataResultBooking.Loading
        viewModelScope.launch {
            try {
                val result = getBookingUseCase.execute(key)
                dataResultBooking.value = when (result) {
                    is DataResultBooking.Success -> {
                        val mappedData =
                            getBookingMapper.GetBookingResponseToGetBookingResponseDto(result.data)

                        // Determine selected date based on application logic
                        val selectedDate = LocalDate.now() // Example: Use the current date

                        // Parse and populate time slots
                        val parsedTimeSlots = mappedData.flatMap { booking ->
                            booking.plannings.mapNotNull { planning ->
                                parseTimeSlot(planning.fromStr, selectedDate)
                            }
                        }

                        // Populate _timeSlots
                        _timeSlots.postValue(parsedTimeSlots)


                        // Filter slots after populating _timeSlots
                        if (parsedTimeSlots.isNotEmpty()) {
                            filterSlotsByDate(selectedDate, parsedTimeSlots)
                            _parsedTimeSlotss.value = parsedTimeSlots
                        } else {
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
                // Get current date and time in local timezone
                val currentDateTime = ZonedDateTime.now(ZoneId.of("Africa/Tunis"))
                val isToday = selectedDate.isEqual(currentDateTime.toLocalDate())

                // Check if parsedTimeSlots is null or empty
                if (parsedTimeSlots.isEmpty()) {
                    _filteredTimeSlots.postValue(emptyList())
                    return@launch
                }

                // Filtering logic
                val filteredSlots = parsedTimeSlots.filter { slot ->
                    try {
                        // Combine LocalDate and LocalTime into ZonedDateTime
                        val slotDateTime = ZonedDateTime.of(
                            slot.date.atTime(slot.time), // Combine LocalDate and LocalTime
                            ZoneId.of("Africa/Tunis") // Ensure this matches your timezone
                        )


                        // Adjusted filtering logic
                        when {
                            isToday -> slotDateTime.isAfter(currentDateTime) // Only keep future slots for today
                            else -> slotDateTime.toLocalDate().isEqual(selectedDate) // Match the selected date
                        }
                    } catch (e: Exception) {
                        false
                    }
                }

                // Log filtered slots for debugging purposes

                // Update _filteredTimeSlots with the filtered slots
                _filteredTimeSlots.postValue(filteredSlots)

            } catch (e: Exception) {
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
