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

    fun getBooking(key: String) {
        dataResultBooking.value = DataResultBooking.Loading
        viewModelScope.launch {
            try {
                val result = getBookingUseCase.execute(key)
                dataResultBooking.value = when (result) {
                    is DataResultBooking.Success -> {
                        val mappedData = getBookingMapper.GetBookingResponseToGetBookingResponseDto(result.data)

                        val uniqueTimeSlots = mutableSetOf<TimeSlot>()
                        val currentDateTime = ZonedDateTime.now(ZoneId.of("GMT+1"))
                        mappedData.forEach { booking ->
                            booking.plannings.forEach { planning ->
                                Log.d("GetBooking", "fromStr: ${planning.fromStr}")
                            }
                        }

                        val timeList = mappedData.flatMap { booking ->
                            booking.plannings.mapNotNull { planning ->
                                val slot = parseTimeSlot(planning.fromStr)

                                if (isTimeSlotValid(slot, currentDateTime) && uniqueTimeSlots.add(slot)) {
                                    slot
                                } else {
                                    null
                                }
                            }
                        }


                        _timeSlots.postValue(timeList)
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
