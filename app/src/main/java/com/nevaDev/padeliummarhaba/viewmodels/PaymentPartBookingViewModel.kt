package com.nevaDev.padeliummarhaba.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.usecases.PaymentGetAvoirUseCase
import com.padelium.domain.usecases.PaymentPartBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PaymentPartBookingViewModel @Inject constructor(private val paymentPartBookingUseCase: PaymentPartBookingUseCase):
    ViewModel(){


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun PaymentPartBooking(paymentPartBookingRequest: PaymentPartBookingRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value=paymentPartBookingUseCase.PaymentPartBooking(paymentPartBookingRequest)
        }
    }

}
