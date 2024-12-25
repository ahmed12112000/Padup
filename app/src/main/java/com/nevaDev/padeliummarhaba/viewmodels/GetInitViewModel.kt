package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.GetInitResponseDTO
import com.padelium.data.mappers.GetInitMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.GetInitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetInitViewModel @Inject constructor(
    private val getInitUseCase: GetInitUseCase,
    private val getInitMapper: GetInitMapper
) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<GetInitResponseDTO>>()

    fun GetInit(key: String) {
        dataResultBooking.value = DataResultBooking.Loading // Set to loading state

        viewModelScope.launch {
            // Call the use case directly with the string key
            val result = getInitUseCase.execute(key)

            // Handle the result of the use case call
            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    // Map the response from GetInitResponse to GetInitResponseDTO
                    val getInitResponseDTO = getInitMapper.GetInitResponseToGetInitResponseDto(result.data)
                    DataResultBooking.Success(getInitResponseDTO)
                }
                is DataResultBooking.Failure -> {
                    // Forward failure details
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
