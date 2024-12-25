package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevaDev.padeliummarhaba.models.EstablishmentDTO
import com.padelium.data.dto.SearchListResponseDTO
import com.padelium.data.mappers.SearchListMapper
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.SearchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SearchListViewModel @Inject constructor(
    private val searchListUseCase: SearchListUseCase,
    private val searchListMapper: SearchListMapper
) : ViewModel() {

    private val establishments4 = MutableStateFlow<List<EstablishmentDTO>>(emptyList())
    val establishments: StateFlow<List<EstablishmentDTO>> = establishments4
    val dataResultBooking = MutableLiveData<DataResultBooking<List<SearchListResponseDTO>>>()



    fun searchList(key: String) {
        // Set to loading state
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            // Call the use case with the string key to fetch data
            val result = searchListUseCase.execute(key)

            // Handle the result from the use case
            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    // Map the response from SearchListResponse to SearchListResponseDTO
                    val searchListResponseDTO = searchListMapper.SearchListResponseToSearchListResponseDto(result.data)
                    // Logging the "name" property from SearchListResponse
                    result.data.forEach { searchListResponse ->
                        Log.d("SearchList", "Name: ${searchListResponse.name}")
                    }
                    // Return success with the mapped DTO
                    DataResultBooking.Success(searchListResponseDTO)
                }

                is DataResultBooking.Failure -> {
                    // Handle failure by forwarding the failure details
                    DataResultBooking.Failure(
                        exception = result.exception,
                        errorCode = result.errorCode,
                        errorMessage = result.errorMessage
                    )
                }
                else -> {
                    // Handle unexpected failure case
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

