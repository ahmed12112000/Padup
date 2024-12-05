package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.usecases.SaveBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SaveBookingViewModel @Inject constructor(private val saveBookingUseCase: SaveBookingUseCase):ViewModel() {


    val dataResult = MutableLiveData<DataResult>()
    val saveBookingRequest = MutableLiveData<List<SaveBookingRequest>>()

    /**
     * Start getting data
     */
    fun SaveBooking(saveBookingRequest: List<SaveBookingRequest>) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value = saveBookingUseCase.SaveBooking(saveBookingRequest)

        }
    }
}







