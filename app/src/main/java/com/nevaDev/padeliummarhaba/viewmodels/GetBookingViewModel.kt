package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    //val getBookingResponseDTO: LiveData<DataResultBooking<List<GetBookingResponseDTO>>> get() = dataResultBooking

    private val _timeSlots = MutableLiveData<List<TimeSlot>>()
    private val _filteredTimeSlots = MutableLiveData<List<TimeSlot>>()
    private val _selectedFromStr = MutableLiveData<String?>()

    val filteredTimeSlots: LiveData<List<TimeSlot>> get() = _filteredTimeSlots
    val selectedFromStr: LiveData<String?> get() = _selectedFromStr

    fun getBooking(key: String) {
        dataResultBooking.value = DataResultBooking.Loading
        viewModelScope.launch {
            try {
                val result = getBookingUseCase.execute(key)
                dataResultBooking.value = when (result) {
                    is DataResultBooking.Success -> {
                        val mappedData = getBookingMapper.GetBookingResponseToGetBookingResponseDto(result.data)
                        _timeSlots.postValue(mappedData.flatMap { booking ->
                            booking.plannings.mapNotNull { planning ->
                                parseTimeSlot(planning.fromStr)
                            }
                        })
                        DataResultBooking.Success(mappedData)
                    }
                    is DataResultBooking.Failure -> {
                        DataResultBooking.Failure(
                            exception = result.exception,
                            errorCode = result.errorCode,
                            errorMessage = result.errorMessage
                        )
                    }
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


    private val _filteredBookingsState = MutableStateFlow<List<GetBookingResponseDTO>?>(null)
    val filteredBookingsState: StateFlow<List<GetBookingResponseDTO>?> = _filteredBookingsState

    fun updateFilteredBooking(establishmentId: Long) {
        val currentBookings = (dataResultBooking.value as? DataResultBooking.Success)?.data
        Log.d("UpdateFilteredBooking", "currentBookings: $currentBookings")
        if (currentBookings == null) {
            Log.d("UpdateFilteredBooking", "No current bookings available.")
            return
        }

        val filteredList = currentBookings.filter { it.establishmentDTO?.id == establishmentId }
        _filteredBookingsState.value = filteredList

        // Log the filtered list
        Log.d("UpdateFilteredBooking", "Filtered List Size: ${filteredList.size}")
        filteredList.forEach { booking ->
        }
    }



    // Update the function signature to accept the selectedTimeSlot parameter
    fun filterSlotsByDate(selectedDate: LocalDate) {
        val currentDateTime = ZonedDateTime.now(ZoneId.systemDefault())

        Log.d("FilterSlots", "Selected Date: $selectedDate, Current Date-Time: $currentDateTime")

        val isToday = selectedDate.isEqual(LocalDate.now())

        val filteredSlots = _timeSlots.value?.filter { slot ->
            val slotTime = try {
                if (slot.time.isNotEmpty()) {
                    ZonedDateTime.of(
                        LocalDate.parse(slot.date),
                        LocalTime.parse(slot.time, DateTimeFormatter.ofPattern("H:mm")),
                        ZoneId.of("GMT+1")
                    )
                } else {
                    null
                }
            } catch (e: DateTimeParseException) {
                Log.e("FilterSlots", "Invalid slot time format: ${slot.time}", e)
                null
            }

            if (isToday) {
                slotTime?.isAfter(currentDateTime) ?: false
            } else {
                slotTime?.toLocalDate()?.isEqual(selectedDate) ?: false
            }
        } ?: emptyList()

        Log.d("FilteredSlots", "Filtered Slots: $filteredSlots")
        _filteredTimeSlots.postValue(filteredSlots)
    }


    private fun parseTimeSlot(fromStr: String): TimeSlot {
        return try {
            if (fromStr.contains(" ")) {
                val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val dateTime = LocalDateTime.parse(fromStr, dateTimeFormatter)
                TimeSlot(
                    date = dateTime.toLocalDate().toString(),
                    time = dateTime.toLocalTime().toString()
                )
            } else {
                val timeFormatter = DateTimeFormatter.ofPattern("H:mm")
                val localTime = LocalTime.parse(fromStr, timeFormatter)
                TimeSlot(
                    date = LocalDate.now().toString(),
                    time = localTime.toString()
                )
            }
        } catch (e: Exception) {
            Log.e("ParseTimeSlot", "Failed to parse fromStr: '$fromStr' with error: ${e.localizedMessage}", e)
            TimeSlot(date = "Invalid", time = fromStr)
        }
    }


    private fun isTimeSlotValid(slot: TimeSlot, currentDateTime: ZonedDateTime): Boolean {
        return try {
            val slotDateTime = ZonedDateTime.of(
                LocalDate.parse(slot.date),
                LocalTime.parse(slot.time, DateTimeFormatter.ofPattern("H:mm")),
                ZoneId.of("GMT+1")
            )
            slotDateTime.isAfter(currentDateTime)
        } catch (e: Exception) {
            Log.e("IsTimeSlotValid", "Failed to validate time slot: ${slot.date} ${slot.time}", e)
            false
        }
    }
}


data class TimeSlot(
    val date: String,
    val time: String
)
