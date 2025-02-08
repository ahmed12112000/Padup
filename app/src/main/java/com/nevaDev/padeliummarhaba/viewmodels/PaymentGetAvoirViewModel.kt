package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.usecases.PaymentGetAvoirUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PaymentGetAvoirViewModel @Inject constructor(private val paymentGetAvoirUseCase: PaymentGetAvoirUseCase):
    ViewModel(){


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun PaymentGetAvoir(paymentGetAvoirRequest: PaymentGetAvoirRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value=paymentGetAvoirUseCase.PaymentGetAvoir(paymentGetAvoirRequest)
        }
    }

}
