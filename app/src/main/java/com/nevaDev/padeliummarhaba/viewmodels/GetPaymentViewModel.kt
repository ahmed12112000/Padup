package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.usecases.GetPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetPaymentViewModel @Inject constructor(private val getPaymentUseCase: GetPaymentUseCase):
    ViewModel(){


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun GetPayment(getPaymentRequest: GetPaymentRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value=getPaymentUseCase.GetPayment(getPaymentRequest)
        }
    }

}

