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
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class SearchListViewModel @Inject constructor(
    private val searchListUseCase: SearchListUseCase,
    private val searchListMapper: SearchListMapper,

) : ViewModel() {

    val dataResultBooking = MutableLiveData<DataResultBooking<List<SearchListResponseDTO>>>()

    fun searchList(key: String) {
        dataResultBooking.value = DataResultBooking.Loading

        viewModelScope.launch {
            val result = searchListUseCase.execute(key)

            dataResultBooking.value = when (result) {
                is DataResultBooking.Success -> {
                    val searchListResponseDTO = searchListMapper.SearchListResponseToSearchListResponseDto(result.data)

                    DataResultBooking.Success(searchListResponseDTO)
                }

                is DataResultBooking.Failure -> {
                    DataResultBooking.Failure(
                        exception = null,
                        errorCode = null,
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


