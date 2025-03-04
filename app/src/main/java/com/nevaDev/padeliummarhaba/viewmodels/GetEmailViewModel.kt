package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.GetEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class GetEmailViewModel @Inject constructor(
    private val getEmailUseCase: GetEmailUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

    // MutableLiveData to handle navigation events
    val navigationEvent = MutableLiveData<String>()

    /**
     * Start processing payment
     */
    fun GetEmail(bookingIds: List<Long>) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = getEmailUseCase.GetEmail(bookingIds)
            dataResult.value = result
            when (result) {
                is DataResult.Success -> {
                    // Handle the result if the email processing is successful
                    Log.d("GetEmail", "PaymentPayAvoir successful: ${result.data}")
                    // You can use result.data which is the Long value
                }
                is DataResult.Failure -> {
                    // Handle the failure case, log the error message
                    Log.e("GetEmail", "PaymentPayAvoir failed: ${result.errorMessage}")

                    // Check for error code and navigate to the error screen if needed
                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                }
                else -> {
                    Log.e("GetEmail", "PaymentPayAvoir unknown state")
                }
            }
        }
    }
}



