package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.usecases.PaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PaymentViewModel @Inject constructor(private val paymentUseCase: PaymentUseCase):ViewModel() {


    val dataResult = MutableLiveData<DataResult>()
    val paymentRequest = MutableLiveData<PaymentRequest>()

    /**
     * Start getting data
     */
    fun Payment(paymentRequest: PaymentRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            try {
                val result = paymentUseCase.Payment(paymentRequest)  // Should return DataResult
                Log.d("PaymentViewModel", "Payment result: $result") // Log the result
                dataResult.value = result
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "Error during payment: ${e.message}")
                dataResult.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,  // You can pass null if you don't have an error code
                    errorMessage = "Payment failed: ${e.message}"  // The error message string
                )            }
        }
    }
}
//PaymentFAILED .JAVA LANG object cannot be cast