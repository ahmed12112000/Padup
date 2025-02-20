package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.InitBookingResponseDTO
import com.padelium.data.mappers.InitBookingMapper
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.InitBookingRequest
import com.padelium.domain.usecases.InitBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class InitBookingViewModel @Inject constructor(
    private val initBookingUseCase: InitBookingUseCase,

) : ViewModel() {

    val dataResult1 = MutableLiveData<DataResult>()

    fun InitBooking(initBookingRequest: InitBookingRequest) {
        viewModelScope.launch {
            dataResult1.postValue(DataResult.Loading)
            val result = initBookingUseCase.InitBooking(initBookingRequest)
            dataResult1.postValue(result)
        }
    }
}

