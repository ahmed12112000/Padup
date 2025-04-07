package com.nevaDev.padeliummarhaba.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.usecases.PaymentGetAvoirUseCase
import com.padelium.domain.usecases.PaymentPartBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PaymentPartBookingViewModel @Inject constructor(private val paymentPartBookingUseCase: PaymentPartBookingUseCase):
    ViewModel() {


    val dataResult = MutableLiveData<DataResult>()
    val paymentStatus = MutableLiveData<Boolean>()


    /**
     * Start getting data
     */
    suspend fun PaymentPartBooking(
        paymentPartBookingRequest: PaymentPartBookingRequest,
        navController: NavController
    ): Boolean {
        return try {
            val result = paymentPartBookingUseCase.PaymentPartBooking(
                paymentPartBookingRequest,
                navController
            )
            when (result) {
                is DataResult2.Success -> result.data // Return true or false
                else -> false // Return false on failure
            }
        } catch (e: Exception) {
            false // Return false if an exception occurs
        }
    }
}
