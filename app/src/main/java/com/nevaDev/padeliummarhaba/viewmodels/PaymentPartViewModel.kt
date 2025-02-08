
package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.usecases.PaymentPartUseCase
import com.padelium.domain.usecases.PaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentPartViewModel @Inject constructor(private val paymentPartUseCase: PaymentPartUseCase) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    fun PaymentPart(paymentRequest: PaymentRequest) {
        Log.d("PaymentViewModel", "Payment method called with orderId: ${paymentRequest.orderId}")

        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            try {
                val result = paymentPartUseCase.PaymentPart(paymentRequest)
                if (result is DataResult.Success && result.data == null) {
                    Log.d("PaymentViewModel", "Payment success with no body")
                }
                dataResult.value = result
            } catch (e: Exception) {
                Log.e("PaymentViewModel", "Error during payment: ${e.message}")
                dataResult.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = "Payment failed: ${e.message}"
                )
            }
        }
    }
}




