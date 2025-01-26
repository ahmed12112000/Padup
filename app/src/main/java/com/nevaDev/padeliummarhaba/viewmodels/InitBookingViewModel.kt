package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.InitBookingResponseDTO
import com.padelium.data.mappers.InitBookingMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.usecases.InitBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
@HiltViewModel
class InitBookingViewModel @Inject constructor(
    private val initBookingUseCase: InitBookingUseCase,
    private val initBookingMapper: InitBookingMapper,
    private val getBookingViewModel: GetBookingViewModel // Inject the GetBookingViewModel
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<List<InitBookingResponseDTO>>>()

    fun InitBooking(key: String, selectedDate: LocalDate) {
        dataResultBooking.value = DataResultBooking.Loading // Set to loading state

        viewModelScope.launch {
            val result = initBookingUseCase.execute(key)

            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val initBookingDTO = initBookingMapper.initBookingResponseToInitBookingResponseDTO(result.data)
                    DataResultBooking.Success(initBookingDTO)

                    // Trigger GetBookingViewModel's getBooking method on success
                    getBookingViewModel.getBooking(key, selectedDate)

                    DataResultBooking.Success(initBookingDTO)

                }
                is DataResultBooking.Failure -> {
                    DataResultBooking.Failure(
                        exception = result.exception,
                        errorCode = result.errorCode,
                        errorMessage = result.errorMessage
                    )
                }
                else -> {
                    DataResultBooking.Failure(
                        exception = null,
                        errorCode = null,
                        errorMessage = "An unexpected error occurred while fetching the initialization data"
                    )
                }
            }
        }
    }
}

