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
import javax.inject.Inject

@HiltViewModel

class KeyViewModel @Inject constructor(
    private val keyUseCase: KeyUseCase,
    private val keyMapper: KeyMapper,
    private val getInitUseCase: GetInitUseCase,
    private val getInitMapper: GetInitMapper,
    private val searchListUseCase: SearchListUseCase,
    private val searchListMapper : SearchListMapper,
    private val initBookingUseCase: InitBookingUseCase,
    private val initBookingMapper : InitBookingMapper,
    private val getBookingUseCase: GetBookingUseCase,
    private val getBookingMapper : GetBookingMapper,
) : ViewModel() {

    private val establishments4 = MutableStateFlow<List<EstablishmentDTO>>(emptyList())
    val establishments: StateFlow<List<EstablishmentDTO>> = establishments4
    val dataResultBooking = MutableLiveData<DataResultBooking<FetchKeyResponseDTO>>()
    val initDataResultBooking = MutableLiveData<DataResultBooking<GetInitResponseDTO>>()
    val searchListDataResultBooking = MutableLiveData<DataResultBooking<List<SearchListResponseDTO>>>()
    val initBookingDataResultBooking = MutableLiveData<DataResultBooking<List<InitBookingResponseDTO>>>()
    val getBookingDataResultBooking = MutableLiveData<DataResultBooking<List<GetBookingResponseDTO>>>() // To store GetBooking results
    val errorMessage = MutableStateFlow<String?>(null)

    /**
     * Fetch reservation key and on success, call GetInit with the key.
     */
    fun getReservationKey(fetchKeyRequest: FetchKeyRequest) {
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = keyUseCase.getReservationKey(fetchKeyRequest)
            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val fetchKeyResponseDTO = keyMapper.fetchKeyResponseToFetchKeyResponseDTO(result.data)



                    DataResultBooking.Success(fetchKeyResponseDTO)
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
                        errorMessage = "An unexpected error occurred while fetching the reservation key"
                    )
                }
            }
        }
    }


    // Function to call GetInit
    private fun fetchInitData(key: String) {
        initDataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = getInitUseCase.execute(key)

            initDataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val getInitResponseDTO = getInitMapper.GetInitResponseToGetInitResponseDto(result.data)
                    DataResultBooking.Success(getInitResponseDTO)
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




    private fun fetchListData1(key: String) {
        searchListDataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = searchListUseCase.execute(key)

            searchListDataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val searchListResponseDTO = searchListMapper.SearchListResponseToSearchListResponseDto(result.data)
                    DataResultBooking.Success(searchListResponseDTO)
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
                        errorMessage = "An unexpected error occurred while fetching the list data"
                    )
                }
            }
        }
    }


    private fun fetchListData2(key: String) {
        initBookingDataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = initBookingUseCase.execute(key)

            initBookingDataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val initBookingResponseDTO = initBookingMapper.initBookingResponseToInitBookingResponseDTO(result.data)
                    DataResultBooking.Success(initBookingResponseDTO)
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
                        errorMessage = "An unexpected error occurred while fetching the list data"
                    )
                }
            }
        }
    }
    private fun fetchListData3(key: String)  {
        getBookingDataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            try {
                val result = getBookingUseCase.execute(key)
                Log.d("GetBookingUseCase", "Result: $result")

                getBookingDataResultBooking.value = when (result) {
                    is DataResultBooking.Success -> {
                        val getBookingResponseDTO = getBookingMapper.GetBookingResponseToGetBookingResponseDto(result.data)
                        Log.d("MappedResponse", "Mapped DTO: $getBookingResponseDTO")
                        DataResultBooking.Success(getBookingResponseDTO)
                    }
                    is DataResultBooking.Failure -> {
                        Log.e("FetchError", "Error Code: ${result.errorCode}, Message: ${result.errorMessage}")
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
                            errorMessage = "An unexpected error occurred while fetching the list data"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("FetchError", "Exception occurred: ${e.message}")
                getBookingDataResultBooking.value = DataResultBooking.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = "An error occurred while fetching the list data"
                )
            }
        }
    }





}




