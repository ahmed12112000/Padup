package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.EstablishmentDTO
import com.padelium.data.dto.FetchKeyResponseDTO
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.dto.GetInitResponseDTO
import com.padelium.data.dto.InitBookingResponseDTO
import com.padelium.data.dto.SearchListResponseDTO
import com.padelium.data.mappers.GetBookingMapper
import com.padelium.data.mappers.GetInitMapper
import com.padelium.data.mappers.InitBookingMapper
import com.padelium.data.mappers.KeyMapper
import com.padelium.data.mappers.SearchListMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.usecase.KeyUseCase
import com.padelium.domain.usecases.GetBookingUseCase
import com.padelium.domain.usecases.GetInitUseCase
import com.padelium.domain.usecases.InitBookingUseCase
import com.padelium.domain.usecases.SearchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
@HiltViewModel
class KeyViewModel @Inject constructor(
    private val keyUseCase: KeyUseCase,
    private val keyMapper: KeyMapper,
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<FetchKeyResponseDTO>>()
    val navigateToErrorScreen = MutableLiveData<Boolean>() // LiveData for navigation signal

    fun getReservationKey(fetchKeyRequest: FetchKeyRequest, selectedDate: LocalDate) {
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = keyUseCase.getReservationKey(fetchKeyRequest)

            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val fetchKeyResponseDTO = keyMapper.fetchKeyResponseToFetchKeyResponseDTO(result.data)
                    DataResultBooking.Success(fetchKeyResponseDTO)
                }

                is DataResultBooking.Failure -> {
                    // Check errorCode and navigate if necessary
                    result.errorCode?.let { errorCode ->
                        if (errorCode != 200) {
                            // Trigger the navigation signal to navigate to the error screen
                            navigateToErrorScreen.value = true
                        }
                    }
                    DataResultBooking.Failure(
                        exception = result.exception,
                        errorCode = result.errorCode,
                        errorMessage = ""
                    )
                }

                else -> {
                    DataResultBooking.Failure(
                        exception = null,
                        errorCode = null,
                        errorMessage = ""
                    )
                }
            }
        }
    }
}
